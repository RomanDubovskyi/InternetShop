package mate.academy.internetshop.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.dao.jdbc.BucketDaoJdbcImpl;
import mate.academy.internetshop.dao.jdbc.ItemDaoJdbcImpl;
import mate.academy.internetshop.dao.jdbc.OrderDaoJdbcImpl;
import mate.academy.internetshop.dao.jdbc.UserDaoJdbcImpl;
import mate.academy.internetshop.service.BucketService;
import mate.academy.internetshop.service.ItemService;
import mate.academy.internetshop.service.OrderService;
import mate.academy.internetshop.service.UserService;
import mate.academy.internetshop.service.impl.BucketServiceImpl;
import mate.academy.internetshop.service.impl.ItemServiceImpl;
import mate.academy.internetshop.service.impl.OrderServiceImpl;
import mate.academy.internetshop.service.impl.UserServiceImpl;
import org.apache.log4j.Logger;

public class Factory {
    private static final Logger LOGGER = Logger.getLogger(Factory.class);
    private static Connection connection;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/internetshop?"
                    + "user=root&password=roman&serverTimezone=UTC");
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Can't establish connection to our DB", e);
        }
    }

    private static BucketService bucketService;
    private static ItemService itemService;
    private static OrderService orderService;
    private static UserService userService;
    private static ItemDao itemDaoJdbc;
    private static BucketDao bucketDaoJdbc;
    private static UserDao userDaoJdbc;
    private static OrderDao orderDaoJdbc;

    public static BucketDao getBucketDao() {
        if (bucketDaoJdbc == null) {
            bucketDaoJdbc = new BucketDaoJdbcImpl(connection);
        }
        return bucketDaoJdbc;
    }

    public static ItemDao getItemDao() {
        if (itemDaoJdbc == null) {
            itemDaoJdbc = new ItemDaoJdbcImpl(connection);
        }
        return itemDaoJdbc;
    }

    public static OrderDao getOrderDao() {
        if (orderDaoJdbc == null) {
            orderDaoJdbc = new OrderDaoJdbcImpl(connection);
        }
        return orderDaoJdbc;
    }

    public static UserDao getUserDao() {
        if (userDaoJdbc == null) {
            userDaoJdbc = new UserDaoJdbcImpl(connection);
        }
        return userDaoJdbc;
    }

    public static BucketService getBucketService() {
        if (bucketService == null) {
            bucketService = new BucketServiceImpl();
        }
        return bucketService;
    }

    public static ItemService getItemService() {
        if (itemService == null) {
            itemService = new ItemServiceImpl();
        }
        return itemService;
    }

    public static OrderService getOrderService() {
        if (orderService == null) {
            orderService = new OrderServiceImpl();
        }
        return orderService;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = new UserServiceImpl();
        }
        return userService;
    }
}
