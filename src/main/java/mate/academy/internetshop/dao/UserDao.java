package mate.academy.internetshop.dao;

import mate.academy.internetshop.model.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> findByToken(String token);
}
