package visa.vttp.paf.stokexCMS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import visa.vttp.paf.stokexCMS.constants.OrderStatus;
import visa.vttp.paf.stokexCMS.model.Order;
import visa.vttp.paf.stokexCMS.repo.OrderBookRepository;


@Service
public class OrderBookService {

    @Autowired
    private OrderBookRepository obRepo;

    public Integer submitNewOrder(Order o) {
        Integer id = o.getOrderID();
        if (null != id) {
            throw new RuntimeException("submitNewOrder received order with orderID: %s, cancelling submission...".formatted(id));
        }
        id = obRepo.submitOrder(o);
        o.setOrderID(id);
        return id;
    }

    public boolean updateOrderStatus(Order o) {
        if (o.getOrderStatus() == OrderStatus.partiallyFulfilled) {
            return obRepo.updateOrderStatus(
                o.getOrderID(), 
                o.getOrderStatus(), 
                o.getUnfulfilledQty()
            );
        } else {
            return obRepo.updateOrderStatus(o.getOrderID(), o.getOrderStatus());
        }
    }

    public List<Order> getOrdersByUsername(String username, Boolean isActive) {
        if (isActive) { return obRepo.getActiveOrdersByUsername(username); }
        return obRepo.getAllOrdersByUsername(username);
    }

    public List<Order> getActiveOrders() {
        return obRepo.getActiveOrders();
    }

}
