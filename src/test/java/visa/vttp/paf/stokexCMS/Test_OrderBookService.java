package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;
import visa.vttp.paf.stokexCMS.service.OrderBookService;
import static visa.vttp.paf.stokexCMS.engine.OrderBookEngine.TICKER_TEST_ONLY;

import java.time.LocalDateTime;

@SpringBootTest
class Test_OrderBookService {

    @Test
    public void test() {
        assertTrue(true);
    }

    @Autowired
    private OrderBookService obSvc;

    @Autowired 
    private OrderBookRepository obRepo;

    private Order testAsk;
    private Order testBid;

    @BeforeEach
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
    
        obRepo.submitOrder(this.testBid);
        obRepo.submitOrder(this.testAsk);
    }
    
    @AfterEach
    private void deleteTestOrders() {
        obRepo.deleteOrdersByTicker(TICKER_TEST_ONLY);
    }


    @Test
    public void submitOrderSuccessful() {
        assertTrue(null != obSvc.submitOrder(this.testBid));
        assertTrue(null != obSvc.submitOrder(this.testAsk));
    }

    @Test
    public void failInvalidOrderType() {
        Order invalidType = this.testAsk;
        invalidType.setOrderType(-1);
        assertThrows(RuntimeException.class, () -> { obSvc.submitOrder(invalidType); });
    }

    @Test
    public void submitCancelOrderSuccessful() {
        Integer id = obSvc.submitOrder(this.testAsk);
        assertTrue(null != id, "Failed to submit preamble test record for cancellation");
        this.testAsk.setOrderStatus(OrderStatus.cancelled);
        this.testAsk.setUpdated(LocalDateTime.now());
        try {
            obSvc.submitOrder(this.testAsk);
        } catch (RuntimeException ex) {
            fail("Failed to process order of type cancelled");
        }
        assertTrue(true);
    }

    @Test 
    public void updateOrderStatusSuccessful() {
        Integer id = obSvc.submitOrder(this.testAsk);
        this.testAsk.setOrderStatus(OrderStatus.partiallyFulfilled);
        this.testAsk.setUnfulfilledQty(50);
        if (obSvc.updateOrderStatus(this.testAsk) != true) {
            fail("Failed to update order of ID %d with partial fulfillment info;".formatted(id));
        }
        Order o = obRepo.getOrderByOrderID(id);
        if (!OrderStatus.partiallyFulfilled.equals(o.getOrderStatus()) || !o.getUnfulfilledQty().equals(50)) {
            fail("SQL does not persist with partial fulfillment info for order of ID %d".formatted(id));
        }
        assertTrue(true);
    }


}
