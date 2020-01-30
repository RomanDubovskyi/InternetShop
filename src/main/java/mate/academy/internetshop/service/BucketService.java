package mate.academy.internetshop.service;

import java.util.List;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;
import mate.academy.internetshop.model.Item;

public interface BucketService extends GenericService<Bucket, Long> {

    void addItem(Bucket bucket, Item item) throws DataProcessingException;

    void deleteItem(Bucket bucket, Item item) throws DataProcessingException;

    Bucket getByOwnerId(Long ownerId) throws DataProcessingException;

    void clear(Bucket bucket) throws DataProcessingException;

    List<Item> getAllItems(Bucket bucket);
}
