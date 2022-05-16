package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.engine.OrderBookEngine;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;
import visa.vttp.paf.stokexCMS.repo.TradesRepository;
import visa.vttp.paf.stokexCMS.service.OrderBookService;
import static visa.vttp.paf.stokexCMS.engine.OrderBookEngine.TICKER_TEST_ONLY;

import java.time.LocalDateTime;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class Test_OrderBookService {

    @Autowired
    private OrderBookService obSvc;

    @Autowired 
    private OrderBookRepository obRepo;

    @Autowired
    private TradesRepository tRepo;

    private Order testAsk;
    private Order testBid;

    @BeforeAll
    private void initTestOrders() {
        this.testBid = new Order();
        this.testBid.setOrderType(OrderType.bid);
        this.testBid.setOrderStatus(OrderStatus.created);
        this.testBid.setTicker(TICKER_TEST_ONLY);
        this.testBid.setUnfulfilledQty(100);
        this.testBid.setPrice(0.01d);
        this.testBid.setUsername("testUser1");
        this.testBid.setCreated(LocalDateTime.now());

        this.testAsk = new Order();
        this.testAsk.setOrderType(OrderType.ask);
        this.testAsk.setOrderStatus(OrderStatus.created);
        this.testAsk.setTicker(TICKER_TEST_ONLY);
        this.testAsk.setUnfulfilledQty(100);
        this.testAsk.setPrice(100.00d);
        this.testAsk.setUsername("testUser2");
        this.testAsk.setCreated(LocalDateTime.now());
    }

    @BeforeEach
    private void removeOrderIDs() {
        this.testBid.setOrderID(null);
        this.testAsk.setOrderID(null);
    }
    
    @AfterEach
    private void deleteTestOrders() {
        tRepo.deleteTradesByTicker(TICKER_TEST_ONLY);
        obRepo.deleteOrdersByTicker(TICKER_TEST_ONLY);
    }


    @Test
    @org.junit.jupiter.api.Order(1)
    public void submitNewOrderSuccessful() {
        assertTrue(null != obSvc.submitNewOrder(this.testBid));
        assertTrue(null != obSvc.submitNewOrder(this.testAsk));
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    public void failInvalidOrderType() {
        Order invalidType = this.testAsk;
        Integer prev_id = invalidType.getOrderID();
        invalidType.setOrderID(null);
        invalidType.setOrderType(-1);
        assertThrows(RuntimeException.class, () -> { obSvc.submitNewOrder(invalidType); });
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    public void updateFulfilledOrderSuccessful() {
        Integer id = obSvc.submitNewOrder(this.testBid);
        this.testBid.setOrderStatus(OrderStatus.fulfilled);
        assertTrue(obSvc.updateOrderStatus(this.testBid));
        Order o = obRepo.getOrderByOrderID(id);
        if (!OrderStatus.fulfilled.equals(o.getOrderStatus()) || !o.getUnfulfilledQty().equals(100)) {
            fail("SQL does not persist with fulfillment info for order of ID %d".formatted(id));
        }
        assertTrue(true);
    }

    @Test 
    @org.junit.jupiter.api.Order(4)
    public void updatePartialOrderSuccessful() {
        Integer id = obSvc.submitNewOrder(this.testAsk);
        this.testAsk.setOrderStatus(OrderStatus.partiallyFulfilled);
        this.testAsk.setUnfulfilledQty(50);
        assertTrue(obSvc.updateOrderStatus(this.testAsk));
        Order o = obRepo.getOrderByOrderID(id);
        if (!OrderStatus.partiallyFulfilled.equals(o.getOrderStatus()) || !o.getUnfulfilledQty().equals(50)) {
            fail("SQL does not persist with partial fulfillment info for order of ID %d".formatted(id));
        }
        assertTrue(true);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    public void updateCancelOrderSuccessful() {
        Integer id = obSvc.submitNewOrder(this.testAsk);
        assertTrue(null != id, "Failed to submit preamble test record for cancellation");
        this.testAsk.setOrderStatus(OrderStatus.cancelled);
        this.testAsk.setUpdated(LocalDateTime.now());
        try {
            obSvc.updateOrderStatus(this.testAsk);
        } catch (RuntimeException ex) {
            fail("Failed to process order of type cancelled");
        }
        assertTrue(true);
    }

}
