package mate.academy.internetshop;

import mate.academy.internetshop.library.Injector;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.service.impl.BucketServiceImpl;
import mate.academy.internetshop.service.impl.ItemServiceImpl;
import mate.academy.internetshop.service.impl.OrderServiceImpl;
import mate.academy.internetshop.service.impl.UserServiceImpl;

import java.util.List;

public class Main {
    static {
        try {
            Injector.injectDependency();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        ItemService itemService = new ItemServiceImpl();
        BucketService bucketService = new BucketServiceImpl();
        OrderService orderService = new OrderServiceImpl();
        UserService userService = new UserServiceImpl();

        Item item  = new Item();
        Bucket bucket = new Bucket();
        Order order = new Order();
        User user = new User();
        userService.create(user);
        item.setPrice(100.00);
        item.setName("Some Phone");
        itemService.create(item);
        orderService.completeOrder(List.of(item), user);
        System.out.println(orderService.getUserOrders(user));
    }
}
