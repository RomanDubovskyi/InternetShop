package mate.academy.internetshop.dao;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.exceptions.DataProcessingException;

public interface GenericDao<T, I> {
    T create(T entity) throws DataProcessingException;

    Optional<T> get(I id) throws DataProcessingException;

    List<T> getAll() throws DataProcessingException;

    T update(T entity) throws DataProcessingException;

    boolean deleteById(I id) throws DataProcessingException;

    boolean delete(T entity) throws DataProcessingException;
}
