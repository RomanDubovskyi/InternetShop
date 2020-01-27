package mate.academy.internetshop.service.impl;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.annotations.Service;
import mate.academy.internetshop.exceptions.DataProcessingException;
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
    public Order create(Order order) throws DataProcessingException {
        return orderDao.create(order);
    }

    @Override
    public List<Order> getAll() throws DataProcessingException {
        return orderDao.getAll();
    }

    @Override
    public Order get(Long orderId) throws DataProcessingException {
        return orderDao.get(orderId)
                .orElseThrow(() -> new NoSuchElementException("Order doesn't exist"));
    }

    @Override
    public Order update(Order order) throws DataProcessingException {
        return orderDao.update(order);
    }

    @Override
    public boolean deleteById(Long orderId) throws DataProcessingException {
        return orderDao.deleteById(orderId);
    }

    @Override
    public boolean delete(Order order) throws DataProcessingException {
        return orderDao.delete(order);
    }

    @Override
    public Order completeOrder(List<Item> items, User user) throws DataProcessingException {
        Order newOrder = new Order();
        newOrder.setOwnerId(user.getUserId());
        newOrder.setItems(items);
        orderDao.create(newOrder);
        return newOrder;
    }

    @Override
    public List<Order> getUserOrders(User user) throws DataProcessingException {
        return  getAll()
                .stream()
                .filter(o -> o.getOwnerId().equals(user.getUserId()))
                .collect(Collectors.toList());
    }
}
