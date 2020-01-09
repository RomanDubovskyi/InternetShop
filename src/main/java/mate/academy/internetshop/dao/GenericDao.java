package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.Item;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T, ID> {
    T create(T entity);

    Optional<T> get(ID id);

    List<T> getAll();

    T update(T entity);

    boolean deleteById(ID id);

    boolean delete(T entity);
}
