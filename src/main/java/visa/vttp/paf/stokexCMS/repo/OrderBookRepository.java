package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.model.Order;

import static visa.vttp.paf.stokexCMS.repo.Queries.*;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderBookRepository {

    @Autowired
    private JdbcTemplate jt;

    public Integer submitOrder(Order o) {
        KeyHolder kh = new GeneratedKeyHolder();
        jt.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, o.getTicker());
            ps.setBigDecimal(2, o.getPrice());
            ps.setInt(3, o.getUnfulfilledQty());
            ps.setInt(4, o.getOrderType());
            ps.setString(5, o.getUsername());
            return ps;
        }, kh);

        
        return kh.getKey().intValue();
    }

    public List<Order> getActiveOrdersByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_ACTIVE_ORDERS_BY_USERNAME, username);

        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            orderList.add((Order) rs.getObject(i));
        }
        return orderList;
    }

    public List<Order> getBidOrdersByTicker(String ticker) {
        List<Order> oLst = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_UNFULFILLED_BID_ORDERS_BY_TICKER, ticker);
        for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
            oLst.add((Order) rs.getObject(i));
        }
        return oLst;
    }

    public boolean updateOrderStatus(Integer orderID, Integer updateType) {
        final int rows = jt.update(
            SQL_UPDATE_ORDER_BY_ORDERID, 
            updateType, 
            LocalDateTime.now(),
            orderID);
        if (rows == 1) { return true; }
        throw new RuntimeException(
            "Failed to update Order of %d with status %d".formatted(
                orderID, 
                updateType
            )
        );
    }

    public boolean updateOrderStatus(
        Integer orderID, 
        Integer updateType, 
        Integer unfulfilledSize
    ) {
        final int rows = jt.update(
            SQL_UPDATE_ORDER_PARTIAL_BY_ORDERID, 
            updateType, 
            unfulfilledSize,
            LocalDateTime.now(),
            orderID);
        if (rows == 1) { return true; }
        throw new RuntimeException(
            "Failed to update Order of %d with status %d and unfulfilled quantity %d".formatted(
                orderID, 
                updateType, 
                unfulfilledSize
            )
        );
    }

    public boolean deleteOrderByFieldAndValue(
        String field,
        String value 
    ) {
        final int rows = jt.update(SQL_DELETE_ORDER_BY_FIELD_AND_VALUE, field, value);
        if (rows == 1) { return true; }
        throw new RuntimeException("Failed to delete Order with %s and %s".formatted(field, value));
    }
    
}
