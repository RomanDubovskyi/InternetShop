package mate.academy.internetshop.service.impl;

import mate.academy.internetshop.dao.BucketDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Inject;
import mate.academy.internetshop.library.Service;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.BucketService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BucketServiceImpl implements BucketService {
    @Inject
    private static BucketDao bucketDao;

    @Override
    public Bucket create(Bucket bucket) {
        return bucketDao.create(bucket);
    }

    @Override
    public Bucket get(Long id) {
        return bucketDao.get(id)
                .orElseThrow(() -> new NoSuchElementException("Bucket doesn't exist"));
    }

    @Override
    public Bucket update(Bucket bucket) {
        if (!Storage.buckets.contains(bucket)) {
            throw new NoSuchElementException("Bucket you're trying to update, doesn't exist");
        }
        return bucketDao.update(bucket);
    }

    @Override
    public boolean delete(Bucket bucket) {
        return bucketDao.delete(bucket);
    }

    @Override
    public boolean delete(Long bucketId) {
        return bucketDao.delete(bucketId);
    }

    @Override
    public void addItem(Bucket bucket, Item item) {
        bucket.getItems().add(item);
    }

    @Override
    public void deleteItem(Bucket bucket, Item item) {
        bucket.getItems().remove(item);
    }

    @Override
    public void clear(Bucket bucket) {
        if (!Storage.buckets.contains(bucket)) {
          throw  new NoSuchElementException("Bucket doesn't exist");
        }
        bucket.setItems(new ArrayList<>());
    }

    @Override
    public List<Item> getAllItems(Bucket bucket) {
        return bucket.getItems();
    }
}
