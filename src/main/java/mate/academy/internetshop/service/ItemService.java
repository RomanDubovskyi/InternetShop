package mate.academy.internetshop.service;

import mate.academy.internetshop.model.Item;

import java.util.List;

public interface ItemService {

    Item create(Item item);

    Item get(Long id);

    List<Item> getAll();

    Item update(Item item);

    boolean deleteById(Long id);

    boolean delete(Item item);
}
