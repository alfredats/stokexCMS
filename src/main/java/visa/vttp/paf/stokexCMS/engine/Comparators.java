package visa.vttp.paf.stokexCMS.engine;

import java.util.Comparator;

import visa.vttp.paf.stokexCMS.model.Order;

public interface Comparators {
    /**
     * Ask offers are sorted by (1) ascending price, and if the price is the same, (2) earliest time
     */
    public static final Comparator<Order> ASK_COMPARATOR = new Comparator<Order>() {
        public int compare(Order a, Order b) {
            int priceCmp = a.getPrice().compareTo(b.getPrice());
            if (priceCmp == 0) {
                return (a.getCreated().isBefore(b.getCreated())) ? -1 : 1;
            }
            return priceCmp;
        }
    };

    /**
     * Bid offers are sorted by (1) descending price, and if the price is the same, (2) earliest time
     */
    public static final Comparator<Order> BID_COMPARATOR = new Comparator<Order>() {
        public int compare(Order a, Order b) {
            int priceCmp = (-1) * a.getPrice().compareTo(b.getPrice());
            if (priceCmp == 0) {
                return (a.getCreated().isBefore(b.getCreated())) ? -1 : 1;
            }
            return priceCmp;
        }
    };
}
