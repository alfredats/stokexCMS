package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;

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
import visa.vttp.paf.stokexCMS.controller.AuthController;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Test_AuthController {
    
    @Autowired
    private AuthController aCon;

    private static String sessKey;

    @Test
    @Order(1)
    public void postAuth_pass() {
        ResponseEntity<String> resp = aCon.postAuth("{ \"username\": \"maysieSim\", \"password\": \"maysieSim\"}");
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
        System.out.println(">>> " + resp.getBody());
        JsonObject json = Json.createReader(new StringReader(resp.getBody())).readObject();
        sessKey = json.getString("sessionKey");
    }

    @Test
    @Order(2)
    public void postAuth_fail() {
        ResponseEntity<String> resp = aCon.postAuth("{ \"username\": \"maysieSim\", \"password\": \"1234\"}");
        assertTrue(resp.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }

    @Test
    @Order(3)
    public void logoutResource_pass() {
        ResponseEntity<String> resp = aCon.logoutResource(sessKey);
        assertTrue(resp.getStatusCode() == HttpStatus.OK);
    }


}
