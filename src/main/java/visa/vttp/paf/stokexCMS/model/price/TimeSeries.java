package visa.vttp.paf.stokexCMS.model.price;

import java.util.List;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RedisHash
public class TimeSeries {
    @TimeToLive
    private long time; 

    private String id;
    private String name;
    private List<PriceTuple> priceData;

    public JsonObject toJson() {
        JsonArrayBuilder priceArr = Json.createArrayBuilder();
        this.priceData.stream()
            .forEach((PriceTuple x) -> {
                JsonObject single = Json.createObjectBuilder()
                    .add("timestamp", x.getDatetime().toString())
                    .add("price", x.getPrice()).build();
                priceArr.add(single);
            });
        return Json.createObjectBuilder()
            .add("symbolInterval", this.getName())
            .add("prices", priceArr)
            .build();
    }

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * @param time the time to set
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return List<PriceTuple> return the priceData
     */
    public List<PriceTuple> getPriceData() {
        return priceData;
    }

    /**
     * @param priceData the priceData to set
     */
    public void setPriceData(List<PriceTuple> priceData) {
        this.priceData = priceData;
    }

}