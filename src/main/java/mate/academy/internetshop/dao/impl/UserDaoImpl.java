package mate.academy.internetshop.dao.impl;

import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.Storage;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.model.User;

@Dao
public class UserDaoImpl implements UserDao {
    private static Long userIdCounter = 1L;

    @Override
    public User create(User user) {
        user.setUserId(userIdCounter);
        Storage.users.add(user);
        userIdCounter++;
        return user;
    }

    @Override
    public List<User> getAll() {
        return Storage.users;
    }

    @Override
    public Optional<User> get(Long userId) {
        return Storage.users
                .stream()
                .filter(i -> i.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public User update(User user) {
        for (int i = 0; i < Storage.users.size(); i++) {
            if (Storage.users.get(i).getUserId().equals(user.getUserId())) {
                Storage.users.get(i).setUserId(user.getUserId());
            }
        }
        return user;
    }

    @Override
    public boolean deleteById(Long userId) {
        for (int i = 0; i < Storage.users.size(); i++) {
            if (Storage.users.get(i).getUserId().equals(userId)) {
                Storage.users.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean delete(User user) {
        return Storage.users.remove(user);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Storage.users.stream()
                .filter(o -> o.getLogin().equals(login))
                .findFirst();
    }
}
