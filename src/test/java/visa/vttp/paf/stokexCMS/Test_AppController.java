package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import javax.security.sasl.AuthenticationException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import visa.vttp.paf.stokexCMS.controller.AppController;
import visa.vttp.paf.stokexCMS.service.AuthService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_AppController {

    @Autowired
    private AuthService aSvc;

    @Autowired
    private AppController aCon;

    private static String sessKey;

    @Test
    @Order(1)
    public void initSess() {
        try {
            sessKey = aSvc.verifyCredentials("maysieSim", "maysieSim");
        } catch (AuthenticationException ex) {
           fail("failed to init session for tests");
        }
        assertTrue(true);
    }

    @Test
    @Order(2)
    public void dashboardResource_pass() {
        ResponseEntity<String> resp = aCon.dashboardResource(sessKey);
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
    }

    @Test
    @Order(3)
    public void portfolioResource_pass() {
        ResponseEntity<String> resp = aCon.portfolioResource(sessKey);
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
    }

}
