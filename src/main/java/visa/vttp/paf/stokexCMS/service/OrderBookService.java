package visa.vttp.paf.stokexCMS.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.engine.OrderBookEngine;
import visa.vttp.paf.stokexCMS.engine.datatypes.Executed;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedCancel;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedTrade;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;
import visa.vttp.paf.stokexCMS.repo.TradesRepository;


@Service
public class OrderBookService {
    private static final Logger logger = LoggerFactory.getLogger(OrderBookService.class);

    @Autowired
    private OrderBookRepository obRepo;

    @Autowired 
    private TradesRepository tRepo;

    @Autowired 
    private static Map<String, OrderBookEngine> obEngineMap;

    @PostConstruct
    private static void init() {
        /**
         * INIT INSTANCES OF ORDERBOOKENGINE FOR EACH TICKER
         */
    }


    public boolean submitOrder(Order o) {
        /**
         * 1) submit order to mysql as backup
         * 2) send order to appropriate orderbookengine
         */
        Integer id;
        if (!(o.getOrderType() == OrderType.cancel)) {
            id = obRepo.submitOrder(o);
            o.setOrderID(id);
        }
        obEngineMap.get(o.getTicker()).processOrder(o);
        return true;
    }

    public boolean updateOrderStatus(Order o) {
        if (o.getOrderStatus() == OrderStatus.partiallyFulfilled) {
            return obRepo.updateOrderStatus(
                o.getOrderID(), 
                o.getOrderStatus(), 
                o.getUnfulfilledQty()
            );
        } else {
            return obRepo.updateOrderStatus(o.getOrderID(), o.getOrderStatus());
        }
    }

    public void syncEngines() {
        for (String ticker : obEngineMap.keySet()) {
            this.syncProcessed(ticker);
        }
    }

    @Transactional
    public void syncProcessed(String ticker) {
        OrderBookEngine obe = obEngineMap.get(ticker);
        List<Executed> ops = obe.getExecutedOps();
        try {
            ops.stream()
                .filter(x -> (x != null))
                .forEach((Executed e) -> {
                    if (e instanceof ExecutedTrade) {
                        ExecutedTrade t = (ExecutedTrade) e;
                        this.updateOrderStatus(t.getBid());
                        this.updateOrderStatus(t.getAsk());
                        tRepo.submitTrade(t);                
                    } else if (e instanceof ExecutedCancel) {
                        ExecutedCancel c = (ExecutedCancel) e;
                        this.updateOrderStatus(c.getCancelledOrder());
                    }
                });
        } catch (RuntimeException ex) {
            logger.error(ex.getStackTrace().toString());
            logger.info(">>> FAILED TO SYNC TRADES FROM %s ENGINE".formatted(ticker));
            throw new RuntimeException();
        }
    }

    
    
}
