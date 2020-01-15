package mate.academy.internetshop.service.impl;

import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.annotations.Inject;
import mate.academy.internetshop.annotations.Service;
import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.model.User;
import mate.academy.internetshop.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
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
       return userDao.findByLogin(login, password).orElseThrow(() ->
               new AuthenticationException("Login or password is incorrect!"));
    }

    @Override
    public User getByToken(String token) throws AuthenticationException {
        return userDao.findByToken(token).orElseThrow(()
                -> new AuthenticationException("Password or Login is incorrect"));
    }
}
