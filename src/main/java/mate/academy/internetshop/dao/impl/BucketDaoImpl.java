package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Dao;
import mate.academy.internetshop.model.Bucket;

import java.util.Optional;

@Dao
public class BucketDaoImpl implements BucketDao {
    private static Long bucketIdCounter = 1L;

    @Override
    public Bucket create(Bucket bucket) {
        bucket.setId(bucketIdCounter);
        Storage.buckets.add(bucket);
        bucketIdCounter++;
        return bucket;
    }

    @Override
    public Optional<Bucket> get(Long id) {
        return Storage.buckets
                .stream()
                .filter(b -> b.getId().equals(id))
                .findFirst();
    }

    @Override
    public Bucket update(Bucket bucket) {
        for (int i = 0; i < Storage.buckets.size(); i++) {
            if (Storage.buckets.get(i).getId().equals(bucket.getId())) {
                Storage.buckets.set(i, bucket);
            }
        }
        return bucket;
    }

    @Override
    public boolean delete(Bucket bucket) {
        for (int i = 0; i < Storage.buckets.size(); i++) {
            if (Storage.buckets.get(i).getId().equals(bucket.getId())) {
                Storage.buckets.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Long bucketId) {
        for (int i = 0; i < Storage.buckets.size(); i++) {
            if (Storage.buckets.get(i).getId().equals(bucketId)) {
                Storage.buckets.remove(i);
                return true;
            }
        }
        return false;
    }
}
