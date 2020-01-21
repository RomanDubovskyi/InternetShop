package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.model.Item;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static Logger logger = Logger.getLogger(ItemDaoJdbcImpl.class);
    private static final String DB_NAME = "internetshop";

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item create(Item item) {
        String itemName = item.getName();
        Double price = item.getPrice();
        String query = "insert into " + DB_NAME + ".items (name, price) values ('"
                + itemName + "', " + price + ");";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Item can't be added", e);
        }
        return item;
    }

    @Override
    public Optional<Item> get(Long id) {
        String query = "select * from " + DB_NAME + ".items where item_id = " + id + ";";
        try (Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Long itemId = rs.getLong("item_id");
                String name = rs.getString("name");
                Double price = rs.getDouble("price");
                Item newItem = new Item();
                newItem.setPrice(price);
                newItem.setName(name);
                newItem.setId(itemId);
                return Optional.of(newItem);
            }
        } catch (SQLException e) {
            logger.warn("Can't get item by id " + id);
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll() {
        String query = "select * from " + DB_NAME + ".items;";
        List<Item> itemList = new ArrayList<>();
        try (Statement statement = connection.createStatement();) {
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                Item item = new Item();
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                item.setId(rs.getLong("item_id"));
                itemList.add(item);
            }
        } catch (SQLException e) {
            logger.warn("Can't get items", e);
        }
        return itemList;
    }

    @Override
    public Item update(Item item) {
        Long id = item.getId();
        String name = item.getName();
        Double price = item.getPrice();
        String query = "update " + DB_NAME + ".items set name = '" + name + "', price = " + price
                + " where item_id = " + id + ";";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.warn("Item couldn't be update", e);
        }
        return item;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = "delete from " + DB_NAME + ".items where item_id =" + id + ";";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.warn("Can't delete item with id " + id);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Item item) {
        String query = "delete from " + DB_NAME + ".items where item_id ="
                + item.getId() + ";";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.warn("Can't delete item with id " + item.getId());
            return false;
        }
        return true;
    }
}
