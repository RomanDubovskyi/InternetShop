package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class BucketDaoJdbcImpl extends AbstractDao<Bucket> implements BucketDao {
    private static final String TABLE_BUCKETS = "buckets";
    private static final String TABLE_BUCKETS_ITEMS = "buckets_items";
    private static final String TABLE_ITEMS = "items";

    public BucketDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Bucket create(Bucket bucket) throws DataProcessingException {
        String queryToBuckets = String.format("INSERT INTO %s (user_id) VALUES (?)", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(
                queryToBuckets, Statement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, bucket.getOwnerID());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                bucket.setBucketId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create bucket " + e);
        }
        return insertToBucketsItems(bucket);
    }

    @Override
    public Optional<Bucket> get(Long id) throws DataProcessingException {
        Bucket bucket = new Bucket(id);
        String getUserIdQuery = String.format(
                "SELECT user_id FROM %s WHERE bucket_id = ?;", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(getUserIdQuery)) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                bucket.setOwnerId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get bucket " + e);
        }
        return getBucketItems(bucket);
    }


    @Override
    public Optional<Bucket> getByOwnerId(Long ownerId) throws DataProcessingException {
        Bucket bucket = new Bucket();
        bucket.setOwnerId(ownerId);
        String getBucketIdQuery = String.format(
                "SELECT bucket_id FROM %s WHERE user_id = ?;", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(getBucketIdQuery)) {
            statement.setLong(1, ownerId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                bucket.setBucketId(rs.getLong("bucket_id"));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get bucket by user_id " + e);
        }
        return getBucketItems(bucket);
    }

    @Override
    public List<Bucket> getAll() throws DataProcessingException {
        List<Bucket> buckets = new ArrayList<>();
        String getBucketsQuery = String.format("SELECT bucket_id FROM %s;", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(getBucketsQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                buckets.add(get(rs.getLong("bucket_id")).get());
            }
            return buckets;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all buckets " + e);
        }
    }

    @Override
    public Bucket update(Bucket bucket) throws DataProcessingException {
        deleteBucketItems(bucket);
        return insertToBucketsItems(bucket);
    }


    @Override
    public boolean deleteById(Long id) throws DataProcessingException {
        return delete(get(id).get());
    }

    @Override
    public boolean delete(Bucket bucket) throws DataProcessingException {
        String deleteBucketQuery = String.format(
                "DELETE FROM %s WHERE bucket_id =?", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(deleteBucketQuery)) {
            statement.setLong(1, bucket.getBucketId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete bucket " + e);
        }
        return deleteBucketItems(bucket);
    }

    private Optional<Bucket> getBucketItems(Bucket bucket) throws DataProcessingException {
        String getItemsQuery = String.format(
                "SELECT * FROM %s JOIN %s ON buckets.bucket_id = buckets_items.bucket_id JOIN %s ON"
                        + " buckets_items.item_id = items.item_id WHERE buckets.bucket_id = ?;",
                TABLE_BUCKETS, TABLE_BUCKETS_ITEMS, TABLE_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(getItemsQuery)) {
            statement.setLong(1, bucket.getBucketId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getLong("item_id"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getDouble("price"));
                bucket.getItems().add(item);
            }
            return Optional.of(bucket);
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get bucket's items " + e);
        }
    }

    private Bucket insertToBucketsItems(Bucket bucket) throws DataProcessingException {
        String queryToBuckets_Items = String.format(
                "INSERT INTO %s (item_id, bucket_id) VALUES (?, ?);", TABLE_BUCKETS_ITEMS);
        for (Item item : bucket.getItems()) {
            try (PreparedStatement statement = connection.prepareStatement(queryToBuckets_Items)) {
                statement.setLong(1, item.getId());
                statement.setLong(2, bucket.getBucketId());
                statement.executeUpdate();
            } catch (SQLException e) {
                throw new DataProcessingException("Can't insert items to bucket " + e);
            }
        }
        return bucket;
    }

    private Boolean deleteBucketItems(Bucket bucket) throws DataProcessingException {
        String deleteBucketItems = String.format(
                "DELETE FROM %s WHERE bucket_id =?;", TABLE_BUCKETS_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(deleteBucketItems)) {
            statement.setLong(1, bucket.getBucketId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete items from bucket " + e);
        }
        return true;
    }
}
