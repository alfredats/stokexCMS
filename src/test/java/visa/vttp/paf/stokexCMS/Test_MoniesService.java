package visa.vttp.paf.stokexCMS;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import visa.vttp.paf.stokexCMS.model.Portfolio;
import visa.vttp.paf.stokexCMS.service.MoniesService;

@SpringBootTest
public class Test_MoniesService {

    @Autowired
    private MoniesService mSvc;
    
    @Test
    public void getPortfolioTimeSeries_pass() {
        List<Portfolio> pList = mSvc.getPortfolioTimeSeries("maysieSim");
        if (pList.isEmpty()) { fail("Failed to get portfolio for user"); }
        assertTrue(true);
    }

    @Test
    public void getPortfolioTimeSeries_fail() {
        try {
            List<Portfolio> pList = mSvc.getPortfolioTimeSeries("");
        } catch (DataAccessException ex) {
            assertTrue(true);
            return;
        }
        fail("result list should be empty");
    }

    @Test
    public void getLatestMonies() {
        Portfolio latest = mSvc.getLatestMonies("maysieSim");
        if (null == latest) { fail("Failed to get latest portfolio values for user"); }
        assertTrue(true);
    } 
    
}
