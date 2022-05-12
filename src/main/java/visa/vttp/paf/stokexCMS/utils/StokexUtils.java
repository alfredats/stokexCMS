package visa.vttp.paf.stokexCMS.utils;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import visa.vttp.paf.stokexCMS.model.price.TimeSeries;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.model.price.PriceTuple;

public class StokexUtils {

    private static DateTimeFormatter genDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter intraDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static TimeSeries createPrice(String jsonResp, boolean isIntraday) {
        if (isIntraday) { return createPrice(jsonResp, intraDtf); }
        return createPrice(jsonResp, genDtf);
    }

    public static TimeSeries createPrice(String jsonResp, DateTimeFormatter dtf) {
        TimeSeries p = new TimeSeries();
        JsonObject json = Json.createReader(new StringReader(jsonResp)).readObject();
        JsonObject metadata = json.getJsonObject("meta");
        JsonArray timeSeries = json.getJsonArray("values");
        
        List<PriceTuple> lpt = new ArrayList<>();
        p.setId(metadata.getString("symbol"));
        timeSeries.stream()
            .map(x -> (JsonObject) x)
            .forEach((JsonObject x) -> {
                PriceTuple pt = new PriceTuple();
                BigDecimal bd = new BigDecimal(x.getString("close"));
                pt.setDatetime(LocalDateTime.parse(x.getString("datetime"), dtf));
                pt.setPrice(bd.setScale(2, RoundingMode.HALF_EVEN));
                lpt.add(pt);
            });
        p.setPriceData(lpt);
        return p;
    }

    public static Order createOrder(SqlRowSet rs) {
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

    
}
