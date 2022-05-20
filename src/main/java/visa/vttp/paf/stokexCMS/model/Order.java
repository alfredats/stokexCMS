package visa.vttp.paf.stokexCMS.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Order {
    private Integer orderID;
    private String username;
    private String ticker;
    private BigDecimal price;
    private Integer unfulfilledQty;
    private Integer orderType;
    private Integer orderStatus;
    private LocalDateTime created;
    private LocalDateTime updated;

    public JsonObject toJson() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        return jsonBuilder.add("orderId", orderID)
            .add("username", username)
            .add("ticker", ticker)
            .add("price", price)
            .add("quantity", unfulfilledQty)
            .add("orderType", orderType)
            .add("orderStatus", orderStatus)
            .add("created", created.toString())
            .add("updated", updated.toString())
            .build();
    }


    @Override
    public String toString() {
        return "Order [created=" + created + ", orderID=" + orderID + ", orderStatus=" + orderStatus + ", orderType="
                + orderType + ", price=" + price + ", ticker=" + ticker + ", unfulfilledQty=" + unfulfilledQty
                + ", updated=" + updated + ", username=" + username + "]";
    }

    public void setPrice(Double d) {
        this.setPrice(new BigDecimal(d));
    }

    /**
     * @return Integer return the orderID
     */
    public Integer getOrderID() {
        return orderID;
    }

    /**
     * @param orderID the orderID to set
     */
    public void setOrderID(Integer orderID) {
        this.orderID = orderID;
    }

    /**
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
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

    /**
     * @return Integer return the unfulfilledQty
     */
    public Integer getUnfulfilledQty() {
        return unfulfilledQty;
    }

    /**
     * @param unfulfilledQty the unfulfilledQty to set
     */
    public void setUnfulfilledQty(Integer unfulfilledQty) {
        this.unfulfilledQty = unfulfilledQty;
    }

    /**
     * @return Integer return the orderType
     */
    public Integer getOrderType() {
        return orderType;
    }

    /**
     * @param orderType the orderType to set
     */
    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    /**
     * @return Integer return the orderStatus
     */
    public Integer getOrderStatus() {
        return orderStatus;
    }

    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    /**
     * @return LocalDateTime return the created
     */
    public LocalDateTime getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    /**
     * @return LocalDateTime return the updated
     */
    public LocalDateTime getUpdated() {
        return updated;
    }

    /**
     * @param updated the updated to set
     */
    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

}
