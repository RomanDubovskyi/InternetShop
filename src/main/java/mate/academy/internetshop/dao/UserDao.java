package mate.academy.internetshop.dao;

import mate.academy.internetshop.exceptions.AuthenticationException;
import mate.academy.internetshop.model.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> findByLogin(String login, String password) throws AuthenticationException;

    Optional<User> findByToken(String token);
}
