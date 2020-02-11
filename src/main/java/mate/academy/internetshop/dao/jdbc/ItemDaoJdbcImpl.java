package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Item;

@Dao
public class ItemDaoJdbcImpl extends AbstractDao<Item> implements ItemDao {
    private static final String TABLE_ITEMS = "items";

    public ItemDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Item create(Item item) throws DataProcessingException {
        String itemName = item.getName();
        Double price = item.getPrice();
        String query = String.format(Locale.ROOT, "INSERT INTO %s (name, price) VALUES (?, ?);",
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
            throw new DataProcessingException("Can't create item " + e);
        }
        return item;
    }

    @Override
    public Optional<Item> get(Long id) throws DataProcessingException {
        String query = String.format("SELECT * FROM %s WHERE item_id =?;", TABLE_ITEMS);
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
            throw new DataProcessingException("Can't get item " + e);
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll() throws DataProcessingException {
        String query = String.format("SELECT * FROM %s;", TABLE_ITEMS);
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
            return itemList;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all items " + e);
        }
    }

    @Override
    public Item update(Item item) throws DataProcessingException {
        Long id = item.getId();
        String name = item.getName();
        Double price = item.getPrice();
        String query = String.format("UPDATE %s SET name =?, price =? WHERE item_id =?;",
                TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setLong(3, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update item " + e);
        }
        return item;
    }

    @Override
    public boolean deleteById(Long id) throws DataProcessingException {
        return delete(get(id).get());
    }

    @Override
    public boolean delete(Item item) throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE item_id =?;", TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete item " + e);
        }
        return true;
    }
}
