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
        String query = String.format("insert into %.items (name, price) values ('%s', %d);",
                DB_NAME, itemName , price);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Item can't be added", e);
        }
        return item;
    }

    @Override
    public Optional<Item> get(Long id) {
        String query = String.format("select * from %s.items where item_id =%d;", DB_NAME, id);
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
        String query = String.format("select * from %s.items;", DB_NAME);
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
        String query = String.format("update %s.items set name = '%s'," +
                " price = %f where item_id = %d;", DB_NAME, name, price, id);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.warn("Item couldn't be update", e);
        }
        return item;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = String.format("delete from %s.items where item_id = %d;", DB_NAME, id);
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
        String query = String.format("delete from %s.items where item_id = %d;", item.getId());
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.warn("Can't delete item with id " + item.getId());
            return false;
        }
        return true;
    }
}
