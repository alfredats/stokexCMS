package visa.vttp.paf.stokexCMS.repo;

public interface Queries {
    public static final String SQL_INSERT_ORDER_WOUT_ORDERID = """
        INSERT INTO orderBook (
            ticker,
            price,
            size,
            order_type,
            username
        ) VALUES 
            (?,?,?,?,?);
            """;
    public static final String SQL_INSERT_ORDER = """
        INSERT INTO orderBook (
            order_id,
            ticker,
            price,
            size,
            order_type,
            username
        ) VALUES 
            (?,?,?,?,?,?);
            """;
    public static final String SQL_GET_ORDER_BY_ORDERID = """
        SELECT 
            order_id,
            ticker, 
            price, 
            size, 
            order_type, 
            order_status, 
            timestamp_created, 
            timestamp_updated,
            username
        FROM orderBook
        WHERE order_id = ?; 
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
    public static final String SQL_GET_UNFULFILLED_ORDERS = """
        SELECT 
            order_id,
            price, 
            size, 
            username,
            timestamp_created
        FROM orderBook
        WHERE order_status < 20
        ORDER BY timestamp_created;
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
    public static final String SQL_DELETE_ORDER_BY_TICKER = """
        DELETE FROM orderBook
        WHERE ticker = ?;
            """;




    /** TRADE QUERIES */
    public static final String SQL_INSERT_EXECUTED_TRADE = """
        INSERT INTO trades(
            bid_id,
            ask_id,
            ticker,
            fulfilledQty,
            fee
        ) VALUES 
            (?,?,?,?,?);
    """;
    public static final String SQL_GET_TRADE_BY_ORDER_ID = """
        SELECT 
            trade_id,
            bid_id,
            ask_id,
            ticker,
            fulfilledQty,
            fee,
            timestamp_created
        FROM trades
        WHERE bid_id = ?
        OR ask_id = ?
    """;
    public static final String SQl_DELETE_TRADES_BY_TICKER = """
        DELETE FROM trades
        WHERE ticker = ?;
    """;

    /** USERS QUERIES */
    public static final String SQL_GET_HASH_BY_LOGIN = """
        SELECT 
            apiKey
        FROM users
        WHERE username = ?
        AND password = UNHEX(SHA1(?));
    """;
    public static final String SQL_GET_USERNAME_BY_APIKEY = """
        SELECT
            username
        FROM users
        WHERE apiKey = UNHEX(SHA1(?));
    """;

}
