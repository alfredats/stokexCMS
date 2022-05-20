package visa.vttp.paf.stokexCMS.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import visa.vttp.paf.stokexCMS.model.Stock;
import visa.vttp.paf.stokexCMS.model.Tuple;
import visa.vttp.paf.stokexCMS.repo.StocksRepo;

@Service
public class StocksService {

    @Autowired
    private StocksRepo sRepo;

    public Tuple<Stock,BigDecimal> getStockByTicker(String ticker) {
        Tuple<Stock,BigDecimal> s;
        ticker = ticker.toUpperCase();
        try {
            s = sRepo.getStockByTicker(ticker);
        } catch (DataAccessException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error: Failed to get stock data with ticker " + ticker) {};
        }
        return s;
    }

    public List<Stock> getAllStocks() {
        return sRepo.getAllStocks_NO_DELTAS();
        // List<Stock> stockList = new ArrayList<>();
        // for (Stock s : sRepo.getAllStocks_NO_DELTAS()) {
        //     stockList.add(this.getStockByTicker(s.getTicker()).getFirst());
        // }
        // return stockList;
    }
    
}
