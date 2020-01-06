package mate.academy.internetshop.service;

import mate.academy.internetshop.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    List<User> getAll();

    User get(Long userId);

    User update(User user);

    boolean deleteById(Long userId);

    boolean delete(User user);
}
