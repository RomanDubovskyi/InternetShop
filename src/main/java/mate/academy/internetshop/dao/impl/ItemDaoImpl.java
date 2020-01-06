package mate.academy.internetshop.dao.impl;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Dao;
import mate.academy.internetshop.model.Item;

import java.util.List;
import java.util.Optional;

@Dao
public class ItemDaoImpl implements ItemDao {
    private static Long itemIdCounter = 1L;

    @Override
    public Item create(Item item) {
        item.setId(itemIdCounter);
        Storage.items.add(item);
        itemIdCounter++;
        return item;
    }

    @Override
    public Optional<Item> get(Long id) {
        return Storage.items
                .stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Item> getAll() {
        return Storage.items;
    }

    @Override
    public Item update(Item item) {
        for (int i = 0; i < Storage.items.size(); i++) {
            if (Storage.items.get(i).getId().equals(item.getId())) {
                Storage.items.get(i).setId(item.getId());
                Storage.items.get(i).setName(item.getName());
                Storage.items.get(i).setPrice(item.getPrice());
            }
        }
        return item;
    }

    @Override
    public boolean deleteById(Long id) {
        for (int i = 0; i < Storage.items.size(); i++) {
            if (Storage.items.get(i).getId().equals(id)) {
                Storage.items.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(Item item) {
      return Storage.items.remove(item);
    }
}
