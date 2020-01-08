package mate.academy.internetshop.service;

import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;

import java.util.List;

public interface OrderService {
    Order create(Order order);

    List<Order> getAll();

    Order get(Long orderId);

    Order update(Order order);

    boolean deleteById(Long orderId);

    boolean delete(Order order);

    Order completeOrder(List<Item> items, User user);

    List<Order> getUserOrders(User user);
}
