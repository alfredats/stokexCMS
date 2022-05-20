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
            username,
            timestamp_created, 
            timestamp_updated
        FROM orderBook
        WHERE order_status < 20
        AND username = ?
        ORDER BY timestamp_updated DESC;
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
        SELECT *
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





    /** STOCKS QUERIES */
    public static final String SQL_GET_STOCK_BY_TICKER = """
        SELECT 
            ticker,
            exchange,
            companyName,
            currentPrice,
            timestamp_created
        FROM stocks
        where ticker = ?
        ORDER BY timestamp_created DESC;
    """;
    public static final String SQL_GET_STOCK_BY_TICKER_LIM_2 = """
        SELECT 
            ticker,
            exchange,
            companyName,
            currentPrice,
            timestamp_created
        FROM stocks
        where ticker = ?
        ORDER BY timestamp_created DESC
        LIMIT 2;
    """;
    public static final String SQL_GET_ALL_STOCKS_MOST_RECENT = """
        SELECT 
            T1.ticker,
            T1.exchange,
            T1.companyName,
            T1.currentPrice,
            T1.timestamp_created
        FROM stocks T1
        INNER JOIN (SELECT  MAX(timestamp_created) AS latest,
                            ticker
                    FROM stocks
                    GROUP BY ticker
                    ) AS T2
        ON T1.ticker = T2.ticker
        AND T1.timestamp_created = T2.latest;
    """;
    public static final String INSERT_STOCK = """
        INSERT INTO stocks (
            ticker,
            exchange,
            companyName,
            currentPrice,
            timestamp_created
        ) VALUES 
            (?, ?, ?, ?, ?);
    """;





    /** SESSION QUERIES */
    public static final String SQL_GET_USERNAME_BY_SESSION_KEY = """
        SELECT
            U.name AS name, 
            US.username as username
        FROM userSessions US 
        INNER JOIN users U
        ON US.username = U.username
        WHERE session_id = ?;
    """;
    public static final String SQL_INSERT_SESSION = """
        INSERT INTO userSessions (
            username,
            session_id,
            timestamp_created,
            timestamp_expired
        ) VALUES 
            (?, ?, ?, ?);
    """; 
    public static final String SQL_GET_SESSION_BY_ID = """
        SELECT * FROM userSessions WHERE session_id = ?;
    """;
    public static final String SQL_UPDATE_SESSION_EXPIRY_BY_ID = """
        UPDATE userSessions 
        SET timestamp_expired = ?
        WHERE session_id = ?;
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
    public static final String SQl_DELETE_TRADES_BY_TICKER = """
        DELETE FROM trades
        WHERE ticker = ?;
    """;








    /** USERS QUERIES */
    public static final String SQL_GET_USERNAME_BY_CREDENTIALS = """
        SELECT
            username
        FROM users 
        WHERE username = ?
        AND password = UNHEX(SHA1(?));
    """;
    





    /** MONIES QUERIES */
    public static final String SQL_GET_MONIES_BY_USERNAME = """
        SELECT
            monies_id,
            portfolio_value,
            available_funds,
            timestamp_created
        FROM monies
        WHERE username = ?
        ORDER BY timestamp_created DESC;
    """;


}
