package visa.vttp.paf.stokexCMS.model.price;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceTuple {
    private LocalDateTime datetime;
    private BigDecimal price;

    @Override
    public String toString() {
        return "PriceTuple [datetime=" + datetime + ", price=" + price + "]";
    }

    /**
     * @return LocalDateTime return the datetime
     */
    public LocalDateTime getDatetime() {
        return datetime;
    }

    /**
     * @param datetime the datetime to set
     */
    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    /**
     * @return BigDecimal return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
