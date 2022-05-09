package visa.vttp.paf.stokexCMS.engine;

import java.util.Comparator;

import visa.vttp.paf.stokexCMS.model.Order;

public interface Comparators {
    public static final Comparator<Order> ORDER_COMPARATOR = new Comparator<Order>() {
        public int compare(Order a, Order b) {
            int priceCmp = a.getPrice().compareTo(b.getPrice());
            if (priceCmp == 0) {
                return (a.getCreated().isBefore(b.getCreated())) ? -1 : 1;
            }
            return priceCmp;

        }
    };
}
