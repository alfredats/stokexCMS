package visa.vttp.paf.stokexCMS.engine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedTrade;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;
import visa.vttp.paf.stokexCMS.repo.TradesRepository;

@Component
public class DumbEngine {
    /**
     * This class is to imitate an actual order matching engine 
     * It indisciminately creates false orders that pair with user-submitted order to simulate trades
     */
    private static final BigDecimal BIGDECIMAL_ZERO = new BigDecimal(0.00d);

    @Autowired
    private OrderBookRepository obRepo;
    @Autowired
    private TradesRepository tRepo;

    private Random r = new Random(System.currentTimeMillis());

    public void processOutstanding() {
        List<Order> allActiveOrders = obRepo.getActiveOrders();
        for (Order o : allActiveOrders) {
            this.processOrder(o);
        }
    }

    @Transactional
    public void processOrder(Order o) {
            Integer matchingID = this.submitMatchingOrder(o);
            Order matchingOrder = obRepo.getOrderByOrderID(matchingID);
            ExecutedTrade et = this.executeMatch(o, matchingOrder);
            tRepo.submitTrade(et);
    }
    
    @Transactional
    private Integer submitMatchingOrder(Order o) {
        Order m = new Order();
        BigDecimal diff = new BigDecimal(r.nextDouble());
        diff = diff.setScale(2, RoundingMode.HALF_EVEN);
        if (o.getOrderType() == OrderType.bid) {
            m.setOrderType(OrderType.ask);
            m.setPrice(o.getPrice().subtract(diff).max(BIGDECIMAL_ZERO));
        } else {
            m.setOrderType(OrderType.bid);
            m.setPrice(o.getPrice().add(diff));
        }
        m.setTicker(o.getTicker());
        m.setUnfulfilledQty(o.getUnfulfilledQty());
        m.setUsername("dumbEngine");
        return obRepo.submitOrder(m);
    }

    @Transactional
    private ExecutedTrade executeMatch(Order o1, Order o2) {
        System.out.println(">>> o1: " + o1);
        System.out.println(">>> o2: " + o2);
        Order bid; Order ask;
        if (o1.getOrderType() == OrderType.bid) { bid = o1; ask = o2; }
        else { bid = o2; ask = o1; }
        bid.setOrderStatus(OrderStatus.fulfilled);
        ask.setOrderStatus(OrderStatus.fulfilled);
        obRepo.updateOrderStatus(bid.getOrderID(), bid.getOrderStatus());
        obRepo.updateOrderStatus(ask.getOrderID(), ask.getOrderStatus());
        ExecutedTrade et = new ExecutedTrade();
        et.setBid(bid.getOrderID()); et.setAsk(ask.getOrderID());
        et.setFulfilledQty(bid.getUnfulfilledQty());
        et.setTicker(bid.getTicker());
        BigDecimal fee = bid.getPrice().subtract(ask.getPrice()).multiply(new BigDecimal(bid.getUnfulfilledQty()));
        et.setFee(fee);
        System.out.println(">>> EXECUTED TRADE: " + et);
        return et;
    }
    
}

