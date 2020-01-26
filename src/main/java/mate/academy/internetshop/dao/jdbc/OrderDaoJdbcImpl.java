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
        String query = String.format("insert into %s (user_id) values (?)", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, order.getOwnerId());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                order.setOrderId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.warn("Can't create order", e);
        }
        return insertToOrderItems(order);
    }

    @Override
    public Optional<Order> get(Long id) {
        Order order = new Order();
        order.setOrderId(id);
        String getUserIdQuery = String.format(
                "select user_id from %s where order_id = ?;", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(getUserIdQuery)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                order.setOwnerId(rs.getLong("user_id"));
            }
        } catch (SQLException e) {
            logger.warn("Can't find order with id" + id);
        }
        return getOrderItems(order);
    }

    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        String getOrderQuery = String.format("select order_id from %s;", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(getOrderQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Order order = get(rs.getLong("order_id")).get();
                orders.add(order);
            }
            return orders;
        } catch (SQLException e) {
            logger.warn("Can't get all Orders", e);
        }
        return null;
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
                "delete from %s where order_id =?", TABLE_ORDERS);
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderQuery)) {
            statement.setLong(1, order.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete order", e);
            return false;
        }
        return deleteOrderItems(order);
    }

    private Order insertToOrderItems(Order order) {
        String ItemsToOrderQuery = String.format(
                "insert into %s (item_id, order_id) values (?, ?);", TABLE_ORDERS_ITEMS);
        for (Item item : order.getItems()) {
            try (PreparedStatement statement = connection.prepareStatement(ItemsToOrderQuery)) {
                statement.setLong(1, item.getId());
                statement.setLong(2, order.getOrderId());
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.warn("Can't create order", e);
            }
        }
        return order;
    }

    private Optional<Order> getOrderItems(Order order) {
        String getItemsQuery = String.format(
                "select * from %s join %s on orders.order_id = orders_items.order_id join %s on" +
                        " orders_items.item_id = items.item_id where orders.order_id = ?;",
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
            logger.warn("Can't find order with id" + order.getOrderId());
        }
        return Optional.empty();
    }

    private Boolean deleteOrderItems(Order order) {
        String deleteOrderItems = String.format(
                "delete from %s where order_id =?;", TABLE_ORDERS_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(deleteOrderItems)) {
            statement.setLong(1, order.getOrderId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't update order", e);
            return false;
        }
        return true;
    }
}
