package visa.vttp.paf.stokexCMS.repo;

public interface Queries {
    public static final String SQL_INSERT_ORDER = """
        INSERT INTO orderBook (
            ticker,
            price,
            size,
            order_type,
            username
        ) VALUES 
            (?,?,?,?,?);
            """;
    public static final String SQL_UPDATE_ORDER_PARTIAL_BY_ORDERID = """
        UPDATE orderBook
        SET order_status = ?,
            size = ?,
            timestamp_updated = ?
        WHERE order_id = ?;
        """;

    public static final String SQL_UPDATE_ORDER_BY_ORDERID = """
        UPDATE orderBook
        SET order_status = ?,
            timestamp_updated = ?
        WHERE order_id = ?;
        """;
    public static final String SQL_GET_ORDERS_BY_USERNAME = """
        SELECT 
            order_id,
            ticker, 
            price, 
            size, 
            order_type, 
            order_status, 
            timestamp_updated 
        FROM orderBook
        WHERE order_status < 20
        AND username = ?
        ORDER BY dateUpdated DESC;
        """;
    
    public static final String SQL_GET_ACTIVE_ORDERS_BY_USERNAME = """
        SELECT 
            order_id,
            ticker, 
            price, 
            size, 
            order_type, 
            order_status, 
            timestamp_created, 
            timestamp_updated 
        FROM orderBook
        WHERE order_status < 20
        AND username = ?
        ORDER BY dateUpdated DESC;
        """;
    public static final String SQL_GET_UNFULFILLED_BID_ORDERS_BY_TICKER = """
        SELECT 
            order_id,
            price, 
            size, 
            username,
            timestamp_created
        FROM orderBook
        WHERE ticker = ?
        AND order_type = 1 
        AND order_status < 20
        ORDER BY price;
        """;
    public static final String SQL_GET_UNFULFILLED_ASK_ORDERS_BY_TICKER = """
        SELECT 
            order_id,
            price, 
            size, 
            username,
            timestamp_created
        FROM orderBook
        WHERE ticker = ?
        AND order_type = 2 
        AND order_status < 20
        ORDER BY price;
        """;
}
