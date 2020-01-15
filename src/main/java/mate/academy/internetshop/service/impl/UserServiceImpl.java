package mate.academy.internetshop.service.impl;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.annotations.Service;
import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private static UserDao userDao;

    @Override
    public User create(User user) {
        user.setToken(getToken());
        return userDao.create(user);
    }

    private String getToken() {
        return UUID.randomUUID().toString();
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
        return userDao.update(user);
    }

    @Override
    public boolean deleteById(Long userId) {
        return userDao.deleteById(userId);
    }

    @Override
    public boolean delete(User user) {
        return userDao.delete(user);
    }

    @Override
    public User login(String login, String password)
            throws AuthenticationException {
       Optional<User> user = userDao.findByLogin(login);
       if (user.isEmpty() || !user.get().getPassword().equals(password)) {
           throw new AuthenticationException("Login or password is incorrect!");
       }
       return user.get();
    }

    @Override
    public Optional<User> getByToken(String token) {
        return userDao.findByToken(token);
    }
}
