package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Dao;
import mate.academy.internetshop.model.Order;

import java.util.List;
import java.util.Optional;

@Dao
public class OrderDaoImpl implements OrderDao {
    private static Long orderIdCounter = 1L;

    @Override
    public Order create(Order order) {
        order.setOrderId(orderIdCounter);
        Storage.orders.add(order);
        orderIdCounter++;
        return order;
    }

    @Override
    public List<Order> getAll() {
        return Storage.orders;
    }

    @Override
    public Optional<Order> get(Long orderId) {
        return Storage.orders
                .stream()
                .filter(i -> i.getOrderId().equals(orderId))
                .findFirst();
    }

    @Override
    public Order update(Order order) {
        for (int i = 0; i < Storage.orders.size(); i++) {
            if (Storage.orders.get(i).getOrderId().equals(order.getOrderId())) {
                Storage.orders.get(i).setItems(order.getItems());
                Storage.orders.get(i).setOrderId(order.getOrderId());
                Storage.orders.get(i).setOwnerId(order.getOwnerId());
            }
        }
        return order;
    }

    @Override
    public boolean deleteById(Long orderId) {
        for (int i = 0; i < Storage.orders.size(); i++) {
            if (Storage.orders.get(i).getOrderId().equals(orderId)) {
                Storage.orders.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Order order) {
        return Storage.orders.remove(order);
    }
}
