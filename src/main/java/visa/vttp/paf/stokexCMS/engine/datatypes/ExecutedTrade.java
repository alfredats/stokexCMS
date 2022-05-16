package visa.vttp.paf.stokexCMS.engine.datatypes;

import java.math.BigDecimal;

public class ExecutedTrade implements Executed {
    private Integer bid;
    private Integer ask;
    private String ticker;
    private Integer fulfilledQty;
    private BigDecimal fee;

    @Override
    public String toString() {
        return "ExecutedTrade [ask=" + ask + ", bid=" + bid + ", fee=" + fee + ", fulfilledQty=" + fulfilledQty
                + ", ticker=" + ticker + "]";
    }

    /**
     * @return Integer return the bid
     */
    public Integer getBid() {
        return bid;
    }

    /**
     * @param bid the bid to set
     */
    public void setBid(Integer bid) {
        this.bid = bid;
    }

    /**
     * @return Integer return the ask
     */
    public Integer getAsk() {
        return ask;
    }

    /**
     * @param ask the ask to set
     */
    public void setAsk(Integer ask) {
        this.ask = ask;
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
     * @return Integer return the fulfilledQty
     */
    public Integer getFulfilledQty() {
        return fulfilledQty;
    }

    /**
     * @param fulfilledQty the fulfilledQty to set
     */
    public void setFulfilledQty(Integer fulfilledQty) {
        this.fulfilledQty = fulfilledQty;
    }

    /**
     * @return BigDecimal return the fee
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * @param fee the fee to set
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

}
