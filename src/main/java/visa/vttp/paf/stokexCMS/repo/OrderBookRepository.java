package visa.vttp.paf.stokexCMS.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.utils.StokexUtils;

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

    
    /* SINGLE ORDER OPERATIONS */
    public Order getOrderByOrderID(Integer orderID) {
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_ORDER_BY_ORDERID, orderID);
        if (!rs.next()) { 
            throw new RuntimeException("Failed to find single record with ID: " + orderID);
        }
        return StokexUtils.createOrder(rs);
    }

    public Integer submitOrder(Order o) {
        KeyHolder kh = new GeneratedKeyHolder();
        jt.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_ORDER_WOUT_ORDERID, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, o.getTicker());
            ps.setBigDecimal(2, o.getPrice());
            ps.setInt(3, o.getUnfulfilledQty());
            ps.setInt(4, o.getOrderType());
            ps.setString(5, o.getUsername());
            return ps;
        }, kh);
        return kh.getKey().intValue();
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




    /* MULTIPLE ORDER OPERATIONS */
    public List<Order> getAllOrdersByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_ORDERS_BY_USERNAME, username);
        while(rs.next()) {
            Order i = StokexUtils.createOrder(rs);
            orderList.add(i);
        }
        return orderList;
    }

    public List<Order> getActiveOrdersByUsername(String username) {
        List<Order> orderList = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_ACTIVE_ORDERS_BY_USERNAME, username);
        while(rs.next()) {
            Order i = StokexUtils.createOrder(rs);
            orderList.add(i);
        }
        return orderList;
    }

    public List<Order> getActiveOrders() {
        List<Order> oLst = new ArrayList<>();
        final SqlRowSet rs = jt.queryForRowSet(SQL_GET_UNFULFILLED_ORDERS);
        while(rs.next()) {
            Order i = StokexUtils.createOrder(rs);
            oLst.add(i);
        }
        return oLst;
    }



    /**
     * NOTE: THIS METHOD MAY OR MAY NOT DELETE RECORDS. USE WITH CAUTION.
     * @param field
     * @param value
     * @return
     */
    public boolean deleteOrdersByTicker(
        String value 
    ) {
        final int rows = jt.update(SQL_DELETE_ORDER_BY_TICKER, value);
        return true; 
    }
    
}
