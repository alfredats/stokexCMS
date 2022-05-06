package visa.vttp.paf.stokexCMS.model;

import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

public class StokexUtils {

    private static DateTimeFormatter genDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter intraDtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Price createPrice(String jsonResp, boolean isIntraday) {
        if (isIntraday) { return createPrice(jsonResp, intraDtf); }
        return createPrice(jsonResp, genDtf);
    }

    public static Price createPrice(String jsonResp, DateTimeFormatter dtf) {
        Price p = new Price();
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

    
}
