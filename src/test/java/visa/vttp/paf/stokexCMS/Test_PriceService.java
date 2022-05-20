package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import visa.vttp.paf.stokexCMS.model.price.TimeSeries;
import visa.vttp.paf.stokexCMS.service.PriceService;

@SpringBootTest
public class Test_PriceService {
    
    @Autowired
    private PriceService pSvc;

    @Test
    public void getAAPLprice() {
        Optional<TimeSeries> aaplTS = pSvc.getPricesByTicker("AAPL", "5min");
        if (aaplTS.isEmpty()) {
            fail("Failed to get price timeseries for AAPL");
        }
    }
}
