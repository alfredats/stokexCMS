package visa.vttp.paf.stokexCMS.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import visa.vttp.paf.stokexCMS.service.OrderBookService;

@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrderBookService obSvc;

    @PostMapping
    public ResponseEntity<String> overviewResource(
        @RequestHeader("x-api-key") String apiKey
    ) {
        return null;
    }

    @PostMapping("/new")
    public ResponseEntity<String> newOrderResource(
        @RequestHeader("x-api-key") String apiKey, 
        @RequestBody MultiValueMap<String, String> body
    ) {
        return null;
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<String> updateOrderResource(
        @RequestHeader("x-api-key") String apiKey,
        @PathVariable String id
    ) {
        return null;
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelOrderResource(
        @RequestHeader("x-api-key") String apiKey,
        @PathVariable String id
    ) {
        return null;
    }
    
    @PostMapping("/active")
    public ResponseEntity<String> activeOrdersResource(
        @RequestHeader("x-api-key") String apiKey
    ) {
        return null;
    }

    @PostMapping("/all")
    public ResponseEntity<String> allOrdersResource(
        @RequestHeader("x-api-key") String apiKey
    ) {
        return null;
    }

}
