package visa.vttp.paf.stokexCMS;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.engine.DumbEngine;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedTrade;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;
import visa.vttp.paf.stokexCMS.repo.TradesRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static visa.vttp.paf.stokexCMS.engine.OrderBookEngine.TICKER_TEST_ONLY;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class Test_DumbEngine {

    @Autowired
    private DumbEngine dEng;
    @Autowired
    private OrderBookRepository obRepo;
    @Autowired
    private TradesRepository tRepo;

    private static Order testBid;
    private static Integer createdOrderID;

    @BeforeAll
    private static void initTestOrder() {
        testBid = new Order();
        testBid.setOrderType(OrderType.bid);
        testBid.setOrderStatus(OrderStatus.created);
        testBid.setTicker(TICKER_TEST_ONLY);
        testBid.setUnfulfilledQty(100);
        testBid.setPrice(0.01d);
        testBid.setUsername("testUser1");
        testBid.setCreated(LocalDateTime.now());
    }


    @Test
    public void test001_tradeExecutes() {
        Integer testBid_ID = obRepo.submitOrder(testBid);
        testBid.setOrderID(testBid_ID);
        dEng.processOrder(testBid);
        ExecutedTrade et = tRepo.getTradeByOrderID(testBid.getOrderID());
        createdOrderID = et.getAsk();
        assertTrue(et != null);
    }

    @Test
    public void test002_processOrderLogsOrderInDB() {
        Order created = obRepo.getOrderByOrderID(createdOrderID);
        assertTrue(created != null);
    }

    @Test
    private void test999_deleteTestOrders() {
        obRepo.deleteOrdersByTicker(TICKER_TEST_ONLY);
    }
    
}
