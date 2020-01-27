package mate.academy.internetshop.dao;

import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long> {
    Optional<User> findByLogin(String login) throws DataProcessingException;

    Optional<User> findByToken(String token) throws DataProcessingException;
}
