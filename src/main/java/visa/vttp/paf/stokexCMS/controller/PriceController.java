package visa.vttp.paf.stokexCMS.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import visa.vttp.paf.stokexCMS.model.Price;
import visa.vttp.paf.stokexCMS.service.PriceService;

@RestController
@RequestMapping("/price")
public class PriceController {
    private static final Logger logger = LoggerFactory.getLogger(PriceController.class);
    
    @Autowired
    private PriceService pSvc;

    @PostMapping("/{ticker}")
    public ResponseEntity<String> priceResource(
        @RequestHeader("x-api-key") String key,
        @PathVariable String ticker,
        @RequestParam String interval
    ) {
        if (!PriceService.ALLOWED_INTERVALS.contains(interval)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{'error': 'only intervals of 5min, 1day, 1week, or 1month allowed'}");
        }
        Price p;
        ticker = ticker.toUpperCase();
        try {
            p = pSvc.getPricesByTicker(ticker, interval).orElseThrow();
        } catch (NoSuchElementException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("{'error': %s}".formatted(ex.getMessage()));
        }

        return ResponseEntity.ok().body(p.toJson().toString());
    }

    
}
