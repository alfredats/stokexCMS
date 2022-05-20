package visa.vttp.paf.stokexCMS.repo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import visa.vttp.paf.stokexCMS.model.Stock;
import visa.vttp.paf.stokexCMS.model.Tuple;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;
import static visa.vttp.paf.stokexCMS.engine.OrderBookEngine.TICKER_TEST_ONLY;

@Repository
public class StocksRepo {

    @Autowired
    private JdbcTemplate jt;

    public List<Stock> getAllStocks_NO_DELTAS() {
        List<Stock> stockList = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_ALL_STOCKS_MOST_RECENT);
        while (rs.next()) {
            if (!rs.getString("ticker").equals(TICKER_TEST_ONLY)) {
                Stock s = StokexUtils.createStock(rs);
                stockList.add(s);
            }
        }
        return stockList;
    }

    /**
     * @param ticker
     * @return Stock data + delta %
     */
    public Tuple<Stock,BigDecimal> getStockByTicker(String ticker) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_STOCK_BY_TICKER_LIM_2, ticker);
        if (!rs.next()) {
            throw new DataAccessException("Error: Failed to find ticker %s in stokex.stocks".formatted(ticker)) {};
        }
        Stock recent = StokexUtils.createStock(rs);
        rs.next();
        Stock prev = StokexUtils.createStock(rs);
        BigDecimal delta = recent.getCurrentPrice().subtract(prev.getCurrentPrice()).divide(prev.getCurrentPrice());
        delta.setScale(3);
        return new Tuple<Stock,BigDecimal>(recent, delta);
    }

}
