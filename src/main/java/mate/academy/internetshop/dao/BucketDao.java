package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Bucket;

import java.util.Optional;

public interface BucketDao {
    Bucket create(Bucket bucket);

    Optional<Bucket> get(Long id);

    Bucket update(Bucket bucket);

    boolean delete(Bucket bucket);

    boolean deleteById(Long bucketId);

}
