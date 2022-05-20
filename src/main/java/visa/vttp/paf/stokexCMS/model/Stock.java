package visa.vttp.paf.stokexCMS.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Stock {
    private String ticker;
    private String exchange;
    private String companyName;
    private BigDecimal currentPrice;
    private LocalDateTime timestamp;

    public JsonObject toJson() {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("ticker", ticker)
            .add("exchange", exchange)
            .add("companyName", companyName)
            .add("price", currentPrice)
            .add("timestamp", timestamp.toString());
        return jb.build();
    }

    @Override
    public String toString() {
        return "Stock [companyName=" + companyName + ", currentPrice=" + currentPrice + ", exchange=" + exchange
                + ", ticker=" + ticker + ", timestamp=" + timestamp + "]";
    }

    /**
     * @return String return the ticker
     */
    public String getTicker() {
        return ticker;
    }

    /**
     * @param ticker the ticker to set
     */
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    /**
     * @return String return the exchange
     */
    public String getExchange() {
        return exchange;
    }

    /**
     * @param exchange the exchange to set
     */
    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    /**
     * @return String return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return BigDecimal return the currentPrice
     */
    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    /**
     * @param currentPrice the currentPrice to set
     */
    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    /**
     * @return LocalDateTime return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }


}
