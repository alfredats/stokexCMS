package visa.vttp.paf.stokexCMS.engine.datatypes;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.model.Order;

public class ExecutedCancel implements Executed {
    private Order cancelledOrder;
    
    public static ExecutedCancel create(Order o) {
        if (o.getOrderStatus() != OrderStatus.cancelled) {
            throw new RuntimeException("Order with order_id " 
                + o.getOrderID() 
                + " is not of type 'cancelled';"
            );
        }
        ExecutedCancel ec = new ExecutedCancel();
        ec.setCancelledOrder(o);
        return ec;
    }

    /**
     * @return Order return the cancelledOrder
     */
    public Order getCancelledOrder() {
        return cancelledOrder;
    }

    /**
     * @param cancelledOrder the cancelledOrder to set
     */
    public void setCancelledOrder(Order cancelledOrder) {
        this.cancelledOrder = cancelledOrder;
    }

}
