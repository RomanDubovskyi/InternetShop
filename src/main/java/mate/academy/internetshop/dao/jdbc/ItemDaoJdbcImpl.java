package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.model.Item;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static Logger logger = Logger.getLogger(ItemDaoJdbcImpl.class);
    private static final String TABLE_ITEMS = "items";

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item create(Item item) {
        String itemName = item.getName();
        Double price = item.getPrice();
        String query = String.format(Locale.ROOT, "insert into %s (name, price) values (?, ?);",
                TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, itemName);
            statement.setDouble(2, price);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                item.setId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.error("Item can't be added", e);
        }
        return item;
    }

    @Override
    public Optional<Item> get(Long id) {
        String query = String.format("select * from %s where item_id =?;", TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
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
        String query = String.format("select * from %s;", TABLE_ITEMS);
        List<Item> itemList = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            ResultSet rs = statement.executeQuery();
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
        String query = String.format("update %s set name =?, price =? where item_id =?;",
                TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setLong(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Item couldn't be update", e);
        }
        return item;
    }

    @Override
    public boolean deleteById(Long id) {
        String query = String.format("delete from %s where item_id = ?", TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete item with id " + id);
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Item item) {
        String query = String.format("delete from %s where item_id =?;", TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete item with id " + item.getId());
            return false;
        }
        return true;
    }
}
