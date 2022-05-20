package visa.vttp.paf.stokexCMS.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Profile;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Portfolio {
    private BigDecimal portfolioValue;
    private BigDecimal availableFunds;
    private LocalDateTime timestampCreated;
    
    public JsonObject toJson() {
        JsonObjectBuilder jb = Json.createObjectBuilder();
        jb.add("portfolioValue", portfolioValue)
            .add("availableFunds", availableFunds)
            .add("timestampCreated", timestampCreated.toString());
        return jb.build();
    }

    @Override
    public String toString() {
        return "Portfolio [availableFunds=" + availableFunds + ", portfolioValue=" + portfolioValue + "]";
    }

    /**
     * @return BigDecimal return the portfolioValue
     */
    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    /**
     * @param portfolioValue the portfolioValue to set
     */
    public void setPortfolioValue(BigDecimal portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    /**
     * @return BigDecimal return the availableFunds
     */
    public BigDecimal getAvailableFunds() {
        return availableFunds;
    }

    /**
     * @param availableFunds the availableFunds to set
     */
    public void setAvailableFunds(BigDecimal availableFunds) {
        this.availableFunds = availableFunds;
    }



    /**
     * @return LocalDateTime return the timestampCreated
     */
    public LocalDateTime getTimestampCreated() {
        return timestampCreated;
    }

    /**
     * @param timestampCreated the timestampCreated to set
     */
    public void setTimestampCreated(LocalDateTime timestampCreated) {
        this.timestampCreated = timestampCreated;
    }

}
