package mate.academy.internetshop.dao;

import mate.academy.internetshop.exceptions.DataProcessingException;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {
    T create(T entity) throws DataProcessingException;

    Optional<T> get(ID id) throws DataProcessingException;

    List<T> getAll() throws DataProcessingException;

    T update(T entity) throws DataProcessingException;

    boolean deleteById(ID id) throws DataProcessingException;

    boolean delete(T entity) throws DataProcessingException;
}
