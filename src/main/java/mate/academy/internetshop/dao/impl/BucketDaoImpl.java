package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Dao;
import mate.academy.internetshop.model.Bucket;

import java.util.List;
import java.util.Optional;

@Dao
public class BucketDaoImpl implements BucketDao {
    private static Long bucketIdCounter = 1L;

    @Override
    public Bucket create(Bucket bucket) {
        bucket.setBucketId(bucketIdCounter);
        Storage.buckets.add(bucket);
        bucketIdCounter++;
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long id) {
        return Storage.buckets
                .stream()
                .filter(b -> b.getBucketId().equals(id))
                .findFirst();
    }

    @Override
    public List<Bucket> getAll() {
        return Storage.buckets;
    }

    @Override
    public Bucket update(Bucket bucket) {
        for (int i = 0; i < Storage.buckets.size(); i++) {
            if (Storage.buckets.get(i).getBucketId().equals(bucket.getBucketId())) {
                Storage.buckets.get(i).setBucketId(bucket.getBucketId());
                Storage.buckets.get(i).setOwnerId(bucket.getOwnerID());
                Storage.buckets.get(i).setItems(bucket.getItems());
            }
        }
        return bucket;
    }

    @Override
    public boolean delete(Bucket bucket) {
        return Storage.buckets.remove(bucket);
    }

    @Override
    public boolean deleteById(Long bucketId) {
        for (int i = 0; i < Storage.buckets.size(); i++) {
            if (Storage.buckets.get(i).getBucketId().equals(bucketId)) {
                Storage.buckets.remove(i);
                return true;
            }
        }
        return false;
    }
}
