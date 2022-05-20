package visa.vttp.paf.stokexCMS.utils;

import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue.ValueType;
import jakarta.json.JsonValue;
import jakarta.json.JsonWriter;
import jakarta.json.JsonWriterFactory;
import static jakarta.json.stream.JsonGenerator.PRETTY_PRINTING;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedTrade;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.model.Portfolio;
import visa.vttp.paf.stokexCMS.model.Stock;

public class StokexUtils {

    public static final JsonWriterFactory jwf = Json.createWriterFactory(Map.of(PRETTY_PRINTING, true));
    public static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Returns a pretty printed json string
     * @param json - JsonObject or JsonValue
     * @return  pretty printed json
     */
    public static final String prettyPrintJson(JsonValue json) {
        StringWriter sw = new StringWriter();
        JsonWriter jw = jwf.createWriter(sw);
        if (json.getValueType() == ValueType.ARRAY) {
            jw.writeArray(json.asJsonArray());
        } else if (json.getValueType() == ValueType.OBJECT) {
            jw.writeObject(json.asJsonObject());
        }
        jw.close();
        return sw.toString();
    }

    public static final Order createOrder(SqlRowSet rs) {
        Order o = new Order();
        o.setOrderID(rs.getInt("order_id"));
        o.setTicker(rs.getString("ticker"));
        o.setPrice(rs.getBigDecimal("price"));
        o.setUnfulfilledQty(rs.getInt("size"));
        o.setOrderType(rs.getInt("order_type"));
        o.setOrderStatus(rs.getInt("order_status"));
        o.setCreated(LocalDateTime.parse(rs.getString("timestamp_created")));
        o.setUpdated(LocalDateTime.parse(rs.getString("timestamp_updated")));
        o.setUsername(rs.getString("username"));
        return o;
    }
    
    public static final Order newOrder(JsonObject req) {
        Order o = new Order();
        // should probaby add a check for 2 decimal points here
        BigDecimal price = req.getJsonNumber("price").bigDecimalValue();
        price.setScale(2, RoundingMode.HALF_EVEN);

        o.setTicker(req.getString("ticker").toUpperCase());
        o.setPrice(price);
        o.setUnfulfilledQty(req.getJsonNumber("size").intValue());
        o.setOrderType(req.getJsonNumber("orderType").intValue());
        return o;
    }

    public static final ExecutedTrade createExecuted(SqlRowSet rs) {
        ExecutedTrade et = new ExecutedTrade();
        et.setBid(rs.getInt("bid_id"));
        et.setAsk(rs.getInt("ask_id"));
        et.setTicker(rs.getString("ticker"));
        et.setFulfilledQty(rs.getInt("fulfilledQty"));
        et.setFee(rs.getBigDecimal("fee"));
        return et;
    }

    public static Stock createStock(SqlRowSet rs) {
        Stock s = new Stock();
        s.setTicker(rs.getString("ticker"));
        s.setExchange(rs.getString("exchange"));
        s.setCompanyName(rs.getString("companyName"));
        s.setCurrentPrice(rs.getBigDecimal("currentPrice"));
        s.setTimestamp(LocalDateTime.parse(rs.getString("timestamp_created"), dtf));
        return s;
    }

    public static Portfolio createPortfolio(SqlRowSet rs) {
        Portfolio pf = new Portfolio();
        pf.setAvailableFunds(rs.getBigDecimal("available_funds"));
        pf.setPortfolioValue(rs.getBigDecimal("portfolio_value"));
        pf.setTimestampCreated(LocalDateTime.parse(rs.getString("timestamp_created"), dtf));
        return pf;
    }



}
