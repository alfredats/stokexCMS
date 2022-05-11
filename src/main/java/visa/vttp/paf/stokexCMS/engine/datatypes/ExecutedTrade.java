package visa.vttp.paf.stokexCMS.engine.datatypes;

import java.math.BigDecimal;

import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.model.Order;

public class ExecutedTrade implements Executed {
    private Order bid;
    private Order ask;
    private String ticker;
    private Integer fulfilledQty;
    private BigDecimal fee;
    
    public static ExecutedTrade create(Order a, Order b) {
        if (!a.getTicker().equals(b.getTicker())) { 
            throw new RuntimeException("Error: Factory method create() of " +
            "ExecutedTrade requires both orders to be for the same Ticker;");
        }
        ExecutedTrade e = new ExecutedTrade();
        if (a.getOrderType() == OrderType.bid && b.getOrderType() == OrderType.ask) {
            e.setBid(a); e.setAsk(b);
            return e;
        } else if (a.getOrderType() == OrderType.ask && b.getOrderType() == OrderType.bid) {
            e.setBid(b); e.setAsk(a);
            return e;
        }
        throw new RuntimeException("Error: Factory Method create() of ExecutedTrace requires 1 bid, 1 ask;");    
    }


    /**
     * @return Order return the bid
     */
    public Order getBid() {
        return bid;
    }

    /**
     * @param bid the bid to set
     */
    public void setBid(Order bid) {
        this.bid = bid;
    }

    /**
     * @return Order return the ask
     */
    public Order getAsk() {
        return ask;
    }

    /**
     * @param ask the ask to set
     */
    public void setAsk(Order ask) {
        this.ask = ask;
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

}
