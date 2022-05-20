package visa.vttp.paf.stokexCMS.controller;

import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.service.AuthService;
import visa.vttp.paf.stokexCMS.service.OrderBookService;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;

@RestController
@RequestMapping(path="/order")
public class OrdersController {

    @Autowired
    private OrderBookService obSvc;
    @Autowired
    private AuthService aSvc;
    
    @PostMapping("/new")
    public ResponseEntity<String> newOrderResource(
        @RequestHeader("x-session-key") String sessKey,
        @RequestBody String body
    ) {
        String[] userDetails;
        try {
           aSvc.verifySess(sessKey); 
           userDetails = aSvc.getUserDetailsBySessKey(sessKey);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"%s\" }".formatted(ex.getMessage()));
        }
        JsonObject jsonBody = Json.createReader(new StringReader(body)).readObject();
        Order o = StokexUtils.newOrder(jsonBody);
        o.setUsername(userDetails[1]);
        Integer id;
        try {
            id = obSvc.submitNewOrder(o);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.internalServerError().body("{'error': 'Error creating new order'}");
        }
        return ResponseEntity.ok().body("{\"orderID\": \"%d\", \"orderStatus\": 10 }".formatted(id));
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrderResource(
        @RequestHeader("x-session-key") String sessKey,
        @PathVariable String id
    ) {
        String[] userDetails;
        try {
           aSvc.verifySess(sessKey); 
           userDetails = aSvc.getUserDetailsBySessKey(sessKey);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"error\": \"%s\" }".formatted(ex.getMessage()));
        }
        String username = userDetails[1];
        Order o;
        try {
            o = obSvc.getOrderByOrderID(Integer.parseInt(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\": \"No such order with ID %s\"}".formatted(id));
        }
        o.setOrderStatus(OrderStatus.cancelled);
        try {
            obSvc.updateOrderStatus(o);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("{\"error\": \"Failed to cancel Order with ID %s\"}".formatted(id));
        }
        return ResponseEntity.ok().body("{\"orderID\": %s, \"orderStatus\": 90 }".formatted(id));
    }

}
