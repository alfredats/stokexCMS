package visa.vttp.paf.stokexCMS.engine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.context.annotation.Bean;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.engine.datatypes.Executed;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedTrade;
import visa.vttp.paf.stokexCMS.engine.datatypes.ExecutedCancel;
import visa.vttp.paf.stokexCMS.model.Order;

import static visa.vttp.paf.stokexCMS.engine.Comparators.*;

public class OrderBookEngine {
    private PriorityQueue<Order> bids = new PriorityQueue<>(BID_COMPARATOR);
    private PriorityQueue<Order> asks = new PriorityQueue<>(ASK_COMPARATOR);
    private List<Executed> executedOps = new ArrayList<>(); 
    private String ticker;
    public static final String TICKER_TEST_ONLY = "TICKERTEST";

    @Bean
    public static OrderBookEngine createOrderBookEngine(String ticker) { return new OrderBookEngine().setTicker(ticker); }

    public boolean processOrder(Order incoming) {
        System.out.println(">>> processing order of id: "+ incoming.getOrderID());
        if (incoming.getOrderType() == OrderType.cancel) {
            Integer orderID = incoming.getOrderID();
            boolean removed = 
                this.removeOrder(orderID, this.getBids()) || this.removeOrder(orderID, this.getAsks());
            if (!removed) { throw new RuntimeException("No such order with orderID found: " + orderID); }
            
            ExecutedCancel ec = ExecutedCancel.create(incoming);
            this.addExecutedOp(ec);
            return true;
        }

        while (this.orderCanProceed(incoming)) {
            Order bookOrd = (incoming.getOrderType() == OrderType.bid) ? this.getAsks().peek() : this.getBids().peek();
            Integer qtyBias = incoming.getUnfulfilledQty() - bookOrd.getUnfulfilledQty();
            Integer fulfilledQty = Math.min(incoming.getUnfulfilledQty(), bookOrd.getUnfulfilledQty());
            ExecutedTrade et = ExecutedTrade.create(incoming, bookOrd);
            if (qtyBias == 0) {
                incoming.setOrderStatus(OrderStatus.fulfilled);
                bookOrd.setOrderStatus(OrderStatus.fulfilled);
            } else if (qtyBias > 0) {
                incoming.setUnfulfilledQty(incoming.getUnfulfilledQty() - fulfilledQty);
                incoming.setOrderStatus(OrderStatus.partiallyFulfilled);     
                bookOrd.setOrderStatus(OrderStatus.fulfilled);
            } else {
                bookOrd.setUnfulfilledQty(bookOrd.getUnfulfilledQty() - fulfilledQty);
                incoming.setOrderStatus(OrderStatus.fulfilled);
                bookOrd.setOrderStatus(OrderStatus.partiallyFulfilled);
            }
            incoming.setUpdated(LocalDateTime.now());
            bookOrd.setUpdated(LocalDateTime.now());
            BigDecimal fee = this.calculateTradeFee(incoming, bookOrd, fulfilledQty);
            et.setFee(fee);
            et.setFulfilledQty(fulfilledQty);
            this.addExecutedOp(et);

            if (qtyBias >= 0) {
                if (bookOrd.getOrderType() == OrderType.ask) { this.getAsks().poll(); }
                else { this.getBids().poll(); }
            }
        }

        if (incoming.getUnfulfilledQty() > 0) {
            if (incoming.getOrderType() == OrderType.bid) { this.addBid(incoming); }
            else { this.addAsk(incoming); }
        }
        return true;
    }


    private BigDecimal calculateTradeFee(Order a, Order b, Integer qty) {
        return a.getPrice().subtract(b.getPrice()).abs().multiply(new BigDecimal(qty)); 
    }
    private boolean orderCanProceed(Order incoming) {
        if (incoming.getOrderType() == OrderType.cancel) { throw new IllegalArgumentException("Cannot accept order of cancel type");}
        if (incoming.getOrderType() == OrderType.bid) {
            return (this.asks.size() > 0) && (incoming.getPrice().compareTo(this.asks.peek().getPrice()) >= 0 );
        }
        return (this.bids.size() > 0) && (incoming.getPrice().compareTo(this.bids.peek().getPrice()) <= 0);
    }

    private boolean removeOrder(Integer orderID, PriorityQueue<Order> oList) {
        for (Order o : oList) {
            if (o.getOrderID() == orderID) {
                oList.remove(o);
                return true;
            }
        }
        return false;
    }

    private void addAsk(Order incoming) {
        this.asks.add(incoming);
    }

    private void addBid(Order incoming) {
        this.bids.add(incoming);
    }
    
    private void addExecutedOp(Executed e) {
        this.executedOps.add(e);
    }

    /**
     * @return List<Executed> return the executedOps
     */
    public List<Executed> getExecutedOps() {
        return executedOps;
    }

    /**
     * @param executedOps the executedOps to set
     */
    public void setExecutedOps(List<Executed> executedOps) {
        this.executedOps = executedOps;
    }


    /**
     * @return PriorityQueue<Order> return the bids
     */
    public PriorityQueue<Order> getBids() {
        return bids;
    }

    /**
     * @param bids the bids to set
     */
    public void setBids(PriorityQueue<Order> bids) {
        this.bids = bids;
    }

    /**
     * @return PriorityQueue<Order> return the asks
     */
    public PriorityQueue<Order> getAsks() {
        return asks;
    }

    /**
     * @param asks the asks to set
     */
    public void setAsks(PriorityQueue<Order> asks) {
        this.asks = asks;
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
    private OrderBookEngine setTicker(String ticker) {
        this.ticker = ticker;
        return this;
    }


}
