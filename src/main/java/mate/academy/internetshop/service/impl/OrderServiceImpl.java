package mate.academy.internetshop.service.impl;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.annotations.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new NoSuchElementException("Order doesn't exist"));
    }

    @Override
    public Order update(Order order) {
        return orderDao.update(order);
    }

    @Override
    public boolean deleteById(Long orderId) {
        return orderDao.deleteById(orderId);
    }

    @Override
    public boolean delete(Order order) {
        return orderDao.delete(order);
    }

    @Override
    public Order completeOrder(List<Item> items, User user) {
        Order newOrder = new Order();
        orderDao.create(newOrder).setItems(items);
        newOrder.setOwnerId(user.getUserId());
        return newOrder;
    }

    @Override
    public List<Order> getUserOrders(User user) {
        return  getAll()
                .stream()
                .filter(o -> o.getOwnerId().equals(user.getUserId()))
                .collect(Collectors.toList());
    }
}
