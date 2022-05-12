package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;
import visa.vttp.paf.stokexCMS.service.OrderBookService;

@SpringBootTest
public class OrderBookServiceTests {

    @Autowired
    private OrderBookService obSvc;

    @Autowired 
    private OrderBookRepository obRepo;

    @BeforeEach
    private void initTestOrders() {
        Order testBid = new Order();
        testBid.setOrderID(1);
        testBid.setOrderType(OrderType.bid);
        testBid.setOrderStatus(OrderStatus.created);
        testBid.setTicker("AAPL");
        testBid.setUnfulfilledQty(100);
        testBid.setPrice(0.01d);
        testBid.setUsername("testUser1");

        Order testAsk = new Order();
        testAsk.setOrderID(2);
        testAsk.setOrderType(OrderType.ask);
        testAsk.setOrderStatus(OrderStatus.created);
        testAsk.setTicker("AAPL");
        testAsk.setUnfulfilledQty(100);
        testAsk.setPrice(100.00d);
        testAsk.setUsername("testUser2");
    
        obRepo.submitOrder(testBid);
        obRepo.submitOrder(testAsk);
    }
    
    // @AfterEach
    // private void deleteTestOrders() {
    //     obRepo.deleteOrderByFieldAndValue("order_id", "1");
    //     obRepo.deleteOrderByFieldAndValue("order_id", "2");
    // }

    @Test
    private void contextLoads() { assertTrue(true);}



}
