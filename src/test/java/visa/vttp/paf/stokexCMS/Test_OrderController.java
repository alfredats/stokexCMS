package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.StringReader;

import javax.security.sasl.AuthenticationException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.controller.OrdersController;
import visa.vttp.paf.stokexCMS.service.AuthService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_OrderController {
    
    @Autowired
    private AuthService aSvc;

    @Autowired
    private OrdersController oCon;

    private static String sessKey;
    private static String orderID;

    @Test
    @Order(1)
    public void initSess() {
        try {
            sessKey = aSvc.verifyCredentials("testUser1", "testPassword1");
        } catch (AuthenticationException ex) {
           fail("failed to init session for tests");
        }
        assertTrue(true);
    }

    @Test
    @Order(2)
    public void newOrderResource_fail() {
        ResponseEntity<String> resp = oCon.newOrderResource("", "");
        assertTrue(resp.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    public void newOrderResource_pass() {
        JsonObject ord = Json.createObjectBuilder()
            .add("price", 0.01)
            .add("ticker", "AAPL")
            .add("size", 10)
            .add("orderType", OrderType.bid)
            .build();
        ResponseEntity<String> resp = oCon.newOrderResource(sessKey, ord.toString());
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
        orderID = Json.createReader(new StringReader(resp.getBody())).readObject().getString("orderID");
    }

    @Test
    @Order(4)
    public void cancelOrderResource_pass() {
        ResponseEntity<String> resp = oCon.cancelOrderResource(sessKey, orderID);
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
    }

    @Test
    @Order(5)
    public void cancelOrderResource_badSessKey() {
        ResponseEntity<String> resp = oCon.cancelOrderResource("", orderID);
        assertTrue(resp.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(6)
    public void cancelOrderResource_badOrderID() {
        ResponseEntity<String> resp = oCon.cancelOrderResource(sessKey, "");
        assertTrue(resp.getStatusCode() == HttpStatus.NOT_FOUND);
    }

}
