package visa.vttp.paf.stokexCMS.engine;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.constants.OrderType;
import visa.vttp.paf.stokexCMS.model.Order;

import static visa.vttp.paf.stokexCMS.engine.Comparators.ORDER_COMPARATOR;

public class OrderBookEngine {
    private PriorityQueue<Order> bids = new PriorityQueue<>(ORDER_COMPARATOR);
    private PriorityQueue<Order> asks = new PriorityQueue<>(ORDER_COMPARATOR);
    private List<ExecutedTrade> trades = new ArrayList<>(); 

    @Bean
    @Scope("singleton")
    public OrderBookEngine createOrderBookEngine() { return new OrderBookEngine(); }
    
    public void processOrder(Order incoming) {
        if (incoming.getOrderType() == OrderType.cancel) {
            Integer orderID = incoming.getOrderID();
            boolean removed = 
                this.removeOrder(orderID, this.getBids()) || this.removeOrder(orderID, this.getAsks());
            if (!removed) { throw new RuntimeException("No such order with orderID found: " + orderID); }
            return;
        }

        while (this.orderCanProceed(incoming)) {
            Order bookOrd = (incoming.getOrderType() == OrderType.bid) ? this.getAsks().peek() : this.getBids().peek();
            Integer qtyBias = incoming.getUnfulfilledQty() - bookOrd.getUnfulfilledQty();
            Integer fulfilledQty = Math.min(incoming.getUnfulfilledQty(), bookOrd.getUnfulfilledQty());
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
            ExecutedTrade et = ExecutedTrade.create(incoming, bookOrd);
            et.setFee(fee);
            et.setFulfilledQty(fulfilledQty);
            this.addTrade(et);

            if (qtyBias >= 0) {
                if (bookOrd.getOrderType() == OrderType.ask) { this.getAsks().poll(); }
                else { this.getBids().poll(); }
            }
        }

        if (incoming.getUnfulfilledQty() > 0) {
            if (incoming.getOrderType() == OrderType.bid) { this.addBid(incoming); }
            else { this.addAsk(incoming); }
        }
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

    public void addTrade(ExecutedTrade trade) {
        this.trades.add(trade);
    }

    /**
     * @return List<ExecutedTrade> return the trades
     */
    public List<ExecutedTrade> getTrades() {
        return trades;
    }

    /**
     * @param trades the trades to set
     */
    public void setTrades(List<ExecutedTrade> trades) {
        this.trades = trades;
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


}
