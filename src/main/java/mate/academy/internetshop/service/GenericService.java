package mate.academy.internetshop.service;

import java.util.List;

public interface GenericService<T, ID> {
    T create(T entity);

    List<T> getAll();

    T get(ID id);

    T update(T entity);

    boolean deleteById(ID id);

    boolean delete(T entity);
}
