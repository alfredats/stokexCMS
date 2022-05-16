package visa.vttp.paf.stokexCMS;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import visa.vttp.paf.stokexCMS.model.Order;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static visa.vttp.paf.stokexCMS.engine.Comparators.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

@SpringBootTest
public class Test_EngineComparator {

    private static List<Order> expectedBidOrder;
    private static List<Order> expectedAskOrder;

    private static Order createTestOrder(Double price, String datetime) {
        Order o = new Order();
        o.setPrice(new BigDecimal(price));
        o.setCreated(LocalDateTime.parse(datetime));
        return o;
    }

    @BeforeAll
    private static void initTestObjs() {
        Order t1 = createTestOrder(10.00d, "2007-12-03T10:00:00");
        Order t2 = createTestOrder(10.00d, "2022-05-09T14:36:00");
        Order t3 = createTestOrder(0.90d, "2022-05-09T14:36:00");
        Order t4 = createTestOrder(99.00d, "2022-05-09T14:36:00");
        expectedAskOrder = Arrays.asList(t3, t1, t2, t4);
        expectedBidOrder = Arrays.asList(t4, t2, t1, t3);
    }

    @Test
    public void bidComparatorSortsCorrectly() {
        PriorityQueue<Order> actual = new PriorityQueue<>(BID_COMPARATOR);
        actual.addAll(expectedBidOrder);
        for (Order e : expectedBidOrder) {
            Order a = actual.poll();
            if (!e.getPrice().equals(a.getPrice()) && e.getCreated().equals(a.getCreated())) {
                fail("Does not meet expected BID order");
            }
        }
        assertTrue(true);
    }

    @Test
    public void askComparatorSortsCorrectly() {
        PriorityQueue<Order> actual = new PriorityQueue<>(ASK_COMPARATOR);
        actual.addAll(expectedAskOrder);
        for (Order e : expectedAskOrder) {
            Order a = actual.poll();
            if (!e.getPrice().equals(a.getPrice()) && e.getCreated().equals(a.getCreated())) {
                fail("Does not meet expected ASK order");
            }
        }
        assertTrue(true);
    }
    
    
}
