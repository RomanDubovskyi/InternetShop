package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
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
public class BucketDaoJdbcImpl extends AbstractDao<Bucket> implements BucketDao {
    private static final String TABLE_BUCKETS = "buckets";
    private static final String TABLE_BUCKETS_ITEMS = "buckets_items";
    private static final String TABLE_ITEMS = "items";
    private static Logger logger = Logger.getLogger(BucketDaoJdbcImpl.class);

    public BucketDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public Bucket create(Bucket bucket) {
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
            logger.error("Can't create bucket", e);
        }
        return insertToBucketsItems(bucket);
    }

    @Override
    public Optional<Bucket> get(Long id) {
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
            logger.error("Can't find bucket with id", e);
        }
        return getBucketItems(bucket);
    }


    @Override
    public Optional<Bucket> getByOwnerId(Long ownerId) {
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
            logger.error("Can't find bucket with user_id", e);
        }
        return getBucketItems(bucket);
    }

    @Override
    public List<Bucket> getAll() {
        List<Bucket> buckets = new ArrayList<>();
        String getBucketsQuery = String.format("SELECT bucket_id FROM %s;", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(getBucketsQuery)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                buckets.add(get(rs.getLong("bucket_id")).get());
            }
            return buckets;
        } catch (SQLException e) {
            logger.error("Can't get all Buckets", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Bucket update(Bucket bucket) {
        deleteBucketItems(bucket);
        return insertToBucketsItems(bucket);
    }


    @Override
    public boolean deleteById(Long id) {
        return delete(get(id).get());
    }

    @Override
    public boolean delete(Bucket bucket) {
        String deleteBucketQuery = String.format(
                "DELETE FROM %s WHERE bucket_id =?", TABLE_BUCKETS);
        try (PreparedStatement statement = connection.prepareStatement(deleteBucketQuery)) {
            statement.setLong(1, bucket.getBucketId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't delete bucket", e);
            return false;
        }
        return deleteBucketItems(bucket);
    }

    private Optional<Bucket> getBucketItems(Bucket bucket) {
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
            logger.error("Can't find bucket with id", e);
        }
        return Optional.empty();
    }

    private Bucket insertToBucketsItems(Bucket bucket) {
        String queryToBuckets_Items = String.format(
                "INSERT INTO %s (item_id, bucket_id) VALUES (?, ?);", TABLE_BUCKETS_ITEMS);
        for (Item item : bucket.getItems()) {
            try (PreparedStatement statement = connection.prepareStatement(queryToBuckets_Items)) {
                statement.setLong(1, item.getId());
                statement.setLong(2, bucket.getBucketId());
                statement.executeUpdate();
            } catch (SQLException e) {
                logger.error("Can't create bucket", e);
            }
        }
        return bucket;
    }

    private Boolean deleteBucketItems(Bucket bucket) {
        String deleteBucketItems = String.format(
                "DELETE FROM %s WHERE bucket_id =?;", TABLE_BUCKETS_ITEMS);
        try (PreparedStatement statement = connection.prepareStatement(deleteBucketItems)) {
            statement.setLong(1, bucket.getBucketId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Can't update bucket", e);
            return false;
        }
        return true;
    }
}
