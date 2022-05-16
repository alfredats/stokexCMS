package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedTrade;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;

@Repository
public class TradesRepository {

    @Autowired
    private JdbcTemplate jt;

    public void submitTrade(ExecutedTrade t) {
        final int rows = jt.update(SQL_INSERT_EXECUTED_TRADE, 
            t.getBid(),
            t.getAsk(),
            t.getTicker(),
            t.getFulfilledQty(),
            t.getFee()
        );
        if (rows != 1) {
            throw new RuntimeException("Failed to upload trade data: " + t.toString());
        }
    }

    public ExecutedTrade getTradeByOrderID(Integer orderID) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_TRADE_BY_ORDER_ID, orderID, orderID);
        if (!rs.next()) {
            throw new RuntimeException("No trade with %d as bid or ask;".formatted(orderID));
        }
        return StokexUtils.createExecuted(rs);
    }
    
}
