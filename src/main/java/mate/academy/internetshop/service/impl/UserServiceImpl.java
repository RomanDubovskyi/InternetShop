package mate.academy.internetshop.service.impl;

import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.library.Inject;
import mate.academy.internetshop.library.Service;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private static UserDao userDao;

    @Override
    public User create(User user) {
        return userDao.create(user);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User get(Long userId) {
        return userDao.get(userId)
                .orElseThrow(()-> new NoSuchElementException("User doesn't exist"));
    }

    @Override
    public User update(User user) {
        if (!Storage.users.contains(user)) {
            throw new NoSuchElementException("User you're trying to update, doesn't exist");
        }
        return userDao.update(user);
    }

    @Override
    public boolean delete(Long userId) {
        return userDao.delete(userId);
    }

    @Override
    public boolean delete(User user) {
        return userDao.delete(user);
    }
}
