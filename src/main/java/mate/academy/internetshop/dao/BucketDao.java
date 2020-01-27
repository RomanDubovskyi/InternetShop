package mate.academy.internetshop.dao;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Bucket;

import java.util.Optional;

public interface BucketDao extends GenericDao<Bucket, Long> {
    Optional<Bucket> getByOwnerId(Long ownerId) throws DataProcessingException;
}
