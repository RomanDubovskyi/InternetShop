package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.OrderDao;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.model.Order;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Dao
public class OrderDaoJdbcImpl extends AbstractDao<Order> implements OrderDao {
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_ORDERS_ITEMS = "orders_items";
    private static final String TABLE_ITEMS = "items";
    private static Logger logger = Logger.getLogger(OrderDaoJdbcImpl.class);

    public OrderDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Order create(Order order) {
        String query = String.format("INSERT INTO %s (user_id) VALUES (?)", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, order.getOwnerId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                order.setOrderId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Can't create order", e);
        }
        return insertToOrderItems(order);
    }

    @Override
    public Optional<Order> get(Long id) {
        Order order = new Order();
        order.setOrderId(id);
        String getUserIdQuery = String.format(
                "SELECT user_id FROM %s WHERE order_id = ?;", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(getUserIdQuery)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                order.setOwnerId(rs.getLong("user_id"));
            }
        } catch (SQLException e) {
            logger.error("Can't find order with id", e);
        }
        return getOrderItems(order);
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String getOrderQuery = String.format("SELECT order_id FROM %s;", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(getOrderQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Order order = get(rs.getLong("order_id")).get();
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            logger.error("Can't get all Orders", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Order update(Order order) {
        deleteOrderItems(order);
        return insertToOrderItems(order);
    }

    @Override
    public boolean deleteById(Long id) {
        if (get(id).isPresent()) {
            delete(get(id).get());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(Order order) {
        String deleteOrderQuery = String.format(
                "DELETE FROM %s WHERE order_id =?", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderQuery)) {
            statement.setLong(1, order.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete order", e);
            return false;
        }
        return deleteOrderItems(order);
    }

    private Order insertToOrderItems(Order order) {
        String ItemsToOrderQuery = String.format(
                "INSERT INTO %s (item_id, order_id) VALUES (?, ?);", TABLE_ORDERS_ITEMS);
        for (Item item : order.getItems()) {
            try (PreparedStatement statement = connection.prepareStatement(ItemsToOrderQuery)) {
                statement.setLong(1, item.getId());
                statement.setLong(2, order.getOrderId());
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Can't create order", e);
            }
        }
        return order;
    }

    private Optional<Order> getOrderItems(Order order) {
        String getItemsQuery = String.format(
                "SELECT * FROM %s JOIN %s ON orders.order_id = orders_items.order_id join %s ON" +
                        " orders_items.item_id = items.item_id WHERE orders.order_id = ?;",
                TABLE_ORDERS, TABLE_ORDERS_ITEMS, TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(getItemsQuery)) {
            statement.setLong(1, order.getOrderId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getLong("item_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                order.getItems().add(item);
            }
            return Optional.of(order);
        } catch (SQLException e) {
            logger.error("Can't find order with id", e);
        }
        return Optional.empty();
    }

    private Boolean deleteOrderItems(Order order) {
        String deleteOrderItems = String.format(
                "DELETE FROM %s WHERE order_id =?;", TABLE_ORDERS_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderItems)) {
            statement.setLong(1, order.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't update order", e);
            return false;
        }
        return true;
    }
}
