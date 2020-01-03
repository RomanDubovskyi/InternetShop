package mate.academy.internetshop.service.impl;

import mate.academy.internetshop.dao.ItemDao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.library.Inject;
import mate.academy.internetshop.library.Service;
import mate.academy.internetshop.model.Item;
import mate.academy.internetshop.service.ItemService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ItemServiceImpl implements ItemService {
    @Inject
    private static ItemDao itemDao;

    @Override
    public Item create(Item item) {
        return itemDao.create(item);
    }

    @Override
    public Item get(Long id) {
        return itemDao.get(id).orElseThrow(()->new NoSuchElementException("Item doesn't exist"));
    }

    @Override
    public List<Item> getAll() {
        return itemDao.getAll();
    }

    @Override
    public Item update(Item item) {
        if (!Storage.items.contains(item)) {
            throw new NoSuchElementException("Item you're trying to update, doesn't exist");
        }
        return itemDao.update(item);
    }

    @Override
    public boolean delete(Long id) {
        return itemDao.delete(id);
    }

    @Override
    public boolean delete(Item item) {
        return itemDao.delete(item);
    }
}
