package mate.academy.internetshop.service.impl;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Inject;
import mate.academy.internetshop.library.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OrderServiceImpl implements OrderService {
    @Inject
    private static OrderDao orderDao;

    @Override
    public Order create(Order order) {
        return orderDao.create(order);
    }

    @Override
    public List<Order> getAll() {
        return orderDao.getAll();
    }

    @Override
    public Order get(Long orderId) {
        return orderDao.get(orderId)
                .orElseThrow(()->new NoSuchElementException("Order doesn't exist"));
    }

    @Override
    public Order update(Order order) {
        if (!Storage.orders.contains(order)) {
            throw new NoSuchElementException("Order you're trying to update, doesn't exist");
        }
        return orderDao.update(order);
    }

    @Override
    public boolean delete(Long orderId) {
        return orderDao.delete(orderId);
    }

    @Override
    public boolean delete(Order order) {
        return orderDao.delete(order);
    }

    @Override
    public Order completeOrder(List<Item> items, User user) {
        Order newOrder = new Order();
        orderDao.create(newOrder);
        newOrder.setItems(items);
        user.getOrdersHistory().add(newOrder);
        return newOrder;
    }

    @Override
    public List<Order> getUserOrders(User user) {
        if (!Storage.users.contains(user)) {
            throw new NoSuchElementException("User you're trying to find, doesn't exist");
        }
        return user.getOrdersHistory();
    }
}
