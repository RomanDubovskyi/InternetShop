package mate.academy.internetshop.dao.jdbc;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Dao
public class UserDaoJdbcImpl extends AbstractDao<User> implements UserDao {
    private static final String TABLE_USERS = "users";
    private static final String TABLE_USERS_ROLES = "users_roles";
    private static final String TABLE_ROLES = "roles";
    private static Logger logger = Logger.getLogger(UserDaoJdbcImpl.class);

    public UserDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public User create(User user) {
        String query = String.format(
                "insert into %s (name, surname, login, password, token) values(?, ?, ?, ?, ?);",
                TABLE_USERS);
        try(PreparedStatement statement = connection.prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getToken());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                user.setUserId(rs.getLong(1));
            }
        } catch (SQLException e) {
            logger.warn("Can't create user", e);
        }
        String setRoleQuery = String.format(
                "insert into %s (user_id, role_id) values(" +
                        "?, (SELECT role_id FROM %s WHERE role_name = ?))",
                TABLE_USERS_ROLES, TABLE_ROLES);
        try(PreparedStatement statement = connection.prepareStatement(setRoleQuery)) {
            statement.setLong(1, user.getUserId());
            statement.setString(2, "USER");
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't set Role", e);
        }
        return user;
    }

    @Override
    public Optional<User> get(Long id) {
        User user = new User();
        user.setUserId(id);
        String query = String.format(
                "select * from %s join %s on users.user_id = users_roles.user_id join %s" +
                        " on users_roles.role_id = roles.role_id where users.user_id =?",
                TABLE_USERS, TABLE_USERS_ROLES, TABLE_ROLES);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getUserId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setToken(rs.getString("token"));
                user.addRole(Role.of(rs.getString("role_name")));
            }
            return Optional.of(user);
        } catch (SQLException e) {
            logger.warn("Can't get user", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        User user = new User();
        user.setLogin(login);
        String query = String.format(
                "select * from %s join %s on users.user_id = users_roles.user_id join %s " +
                        "on users_roles.role_id = roles.role_id where login =?",
                TABLE_USERS, TABLE_USERS_ROLES, TABLE_ROLES);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setUserId(rs.getLong("user_id"));
                user.setPassword(rs.getString("password"));
                user.setToken(rs.getString("token"));
                user.addRole(Role.of(rs.getString("role_name")));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            logger.warn("Can't get user with login "+ login);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByToken(String token) {
        User user = new User();
        user.setToken(token);
        String query = String.format(
                "select * from %s join %s on users.user_id = users_roles.user_id join %s on users_roles.role_id = roles.role_id where token =?;",
                TABLE_USERS, TABLE_USERS_ROLES, TABLE_ROLES);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, token);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user.setName(rs.getString("name"));
                user.setSurname(rs.getString("surname"));
                user.setUserId(rs.getLong("user_id"));
                user.setPassword(rs.getString("password"));
                user.setLogin(rs.getString("login"));
                user.addRole(Role.of(rs.getString("role_name")));
            }
            return Optional.of(user);
        } catch (SQLException e) {
            logger.warn("Can't get user with token " + token);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = String.format(
                "select user_id, name, surname, login, password, token from %s;", TABLE_USERS);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong(1));
                user.setName(rs.getString(2));
                user.setSurname(rs.getString(3));
                user.setLogin(rs.getString(4));
                user.setPassword(rs.getString(5));
                user.setToken(rs.getString(6));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.warn("Can't get all users", e);
        }
        return null;
    }

    @Override
    public User update(User user) {
        String query = String.format("update %s set name =?, surname =?," +
                " login =?, password =?, token =? where user_id =?;", TABLE_USERS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getToken());
            statement.setLong(6, user.getUserId());
            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            logger.warn("Can't update user with id " + user.getUserId());
        }
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return delete(get(id).get());
    }

    @Override
    public boolean delete(User user) {
        String query = String.format("delete from %s where user_id =?;", TABLE_USERS);
        try(PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.warn("Can't delete user", e);
        }
        return false;
    }
}
