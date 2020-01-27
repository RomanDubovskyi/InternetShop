package mate.academy.internetshop.service;

import mate.academy.internetshop.exceptions.DataProcessingException;

import java.util.List;

public interface GenericService<T, ID> {
    T create(T entity) throws DataProcessingException;

    List<T> getAll() throws DataProcessingException;

    T get(ID id) throws DataProcessingException;

    T update(T entity) throws DataProcessingException;

    boolean deleteById(ID id) throws DataProcessingException;

    boolean delete(T entity) throws DataProcessingException;
}
