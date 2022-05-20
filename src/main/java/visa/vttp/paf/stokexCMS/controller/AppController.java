package visa.vttp.paf.stokexCMS.controller;

import java.util.List;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonWriter;
import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.model.Portfolio;
import visa.vttp.paf.stokexCMS.model.Stock;
import visa.vttp.paf.stokexCMS.service.AuthService;
import visa.vttp.paf.stokexCMS.service.MoniesService;
import visa.vttp.paf.stokexCMS.service.OrderBookService;
import visa.vttp.paf.stokexCMS.service.StocksService;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;


@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired 
    private AuthService aSvc;

    @Autowired
    private MoniesService mSvc;

    @Autowired
    private StocksService sSvc;

    @Autowired
    private OrderBookService obSvc;

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboardResource(
        @RequestHeader("x-session-key") String sessKey 
    ) {
        /**
         * dashboard props;
         *  - portfolio value
         *  - available funds
         *  - watchlist
         *  - market summary 
        */        
        try {
            aSvc.verifySess(sessKey);
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("" + ex.getMessage());
        }
        JsonArrayBuilder ja = Json.createArrayBuilder();
        JsonObjectBuilder jb = Json.createObjectBuilder();

        String[] userDetails = aSvc.getUserDetailsBySessKey(sessKey);
        String username = userDetails[1];
        Portfolio pv = mSvc.getLatestMonies(username);
        List<Stock> sList = sSvc.getAllStocks();

        for (Stock s : sList) {
            ja = ja.add(s.toJson());
        }
        JsonArray jsonArr = ja.build();
        
        jb.add("name", userDetails[0])
            .add("monies", pv.toJson())
            .add("watchList", jsonArr)
            .add("market", jsonArr);

        String ppJson = StokexUtils.prettyPrintJson(jb.build());

        return ResponseEntity.ok().body(ppJson);
    }

    @GetMapping("/portfolio")
    public ResponseEntity<String> portfolioResource(
        @RequestHeader("x-session-key") String sessKey 
    ) {
        /**
         * portfolio props:
         *  - portfolio value
         *  - delta value
         *  - portfolio time series
         *  - active orders
         *  - completed orders
         */
        try {
            aSvc.verifySess(sessKey);
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("" + ex.getMessage());
        }
        JsonArrayBuilder actvOrds = Json.createArrayBuilder();
        JsonArrayBuilder cmpltdOrds = Json.createArrayBuilder();
        JsonArrayBuilder portfolioTimeSeries = Json.createArrayBuilder();
        JsonObjectBuilder orders = Json.createObjectBuilder();
        JsonObjectBuilder monies = Json.createObjectBuilder();
        JsonObjectBuilder respBody = Json.createObjectBuilder();

        String[] userDetails = aSvc.getUserDetailsBySessKey(sessKey);
        String username = userDetails[1];
        Portfolio pv = mSvc.getLatestMonies(username);
        List<Portfolio> timeSeries = mSvc.getPortfolioTimeSeries(username);
        List<Order> ords = obSvc.getOrdersByUsername(username);

        for (Order o : ords) {
            if (o.getOrderStatus() < OrderStatus.fulfilled ) {
                actvOrds.add(o.toJson());
            } else {
                cmpltdOrds.add(o.toJson());
            }
        }

        for (Portfolio p : timeSeries) {
            portfolioTimeSeries.add(p.toJson());
        }

        orders.add("active", actvOrds.build())
              .add("completed", cmpltdOrds.build());

        monies.add("latest", pv.toJson())
              .add("timeSeries", portfolioTimeSeries.build());

        respBody.add("name", userDetails[0])
            .add("monies", monies.build())
            .add("orders", orders.build());

        String ppJson = StokexUtils.prettyPrintJson(respBody.build());

        return ResponseEntity.ok().body(ppJson);
    }
    
}
