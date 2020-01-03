package mate.academy.internetshop.service;

import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;

import java.util.List;

public interface BucketService {
    Bucket create(Bucket bucket);

    Bucket get(Long id);

    Bucket update(Bucket bucket);

    boolean delete(Bucket bucket);

    boolean delete(Long bucketId);

    void addItem(Bucket bucket, Item item);

    void deleteItem(Bucket bucket, Item item);

    void clear(Bucket bucket);

    List<Item> getAllItems(Bucket bucket);


}
