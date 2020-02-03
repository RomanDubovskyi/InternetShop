package mate.academy.internetshop.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mate.academy.internetshop.annotations.Dao;
import mate.academy.internetshop.dao.UserDao;
import mate.academy.internetshop.exceptions.DataProcessingException;
import mate.academy.internetshop.model.Role;
import mate.academy.internetshop.model.User;

@Dao
public class UserDaoJdbcImpl extends AbstractDao<User> implements UserDao {
    private static final String TABLE_USERS = "users";
    private static final String TABLE_USERS_ROLES = "users_roles";
    private static final String TABLE_ROLES = "roles";

    public UserDaoJdbcImpl(Connection connection) {
        super(connection);
    }

    @Override
    public User create(User user) throws DataProcessingException {
        String query = String.format(
                "INSERT INTO %s (name, surname, login, password, token, salt)"
                        + " VALUES(?, ?, ?, ?, ?, ?);", TABLE_USERS);
        try (PreparedStatement statement = connection.prepareStatement(
                query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getToken());
            statement.setBytes(6, user.getSalt());
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            while (rs.next()) {
                user.setUserId(rs.getLong(1));
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't create user " + e);
        }
        String setRoleQuery = String.format(
                "INSERT INTO %s (user_id, role_id) VALUES("
                        + "?, (SELECT role_id FROM %s WHERE role_name = ?))",
                TABLE_USERS_ROLES, TABLE_ROLES);
        try (PreparedStatement statement = connection.prepareStatement(setRoleQuery)) {
            statement.setLong(1, user.getUserId());
            statement.setString(2, "USER");
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't set role " + e);
        }
        return user;
    }

    @Override
    public Optional<User> get(Long id) throws DataProcessingException {
        User user = new User();
        user.setUserId(id);
        String query = String.format(
                "SELECT * FROM %s JOIN %s ON users.user_id = users_roles.user_id JOIN %s"
                        + " ON users_roles.role_id = roles.role_id WHERE users.user_id =?",
                TABLE_USERS, TABLE_USERS_ROLES, TABLE_ROLES);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getUserId());
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                setUsersFields(rs, user);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get role " + e);
        }
        return Optional.empty();
    }

    private void setUsersFields(ResultSet rs, User user) throws SQLException {
        user.setUserId(rs.getLong("user_id"));
        user.setName(rs.getString("name"));
        user.setSurname(rs.getString("surname"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setToken(rs.getString("token"));
        user.addRole(Role.of(rs.getString("role_name")));
        user.setSalt(rs.getBytes("salt"));
    }

    @Override
    public Optional<User> findByLogin(String login) throws DataProcessingException {
        User user = new User();
        user.setLogin(login);
        String query = String.format(
                "SELECT * FROM %s JOIN %s ON users.user_id = users_roles.user_id JOIN %s "
                        + "ON users_roles.role_id = roles.role_id WHERE login =?",
                TABLE_USERS, TABLE_USERS_ROLES, TABLE_ROLES);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                setUsersFields(rs, user);
                return Optional.of(user);
            }
        } catch (SQLException e) {
            throw new DataProcessingException("Can't find user by login " + e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAll() throws DataProcessingException {
        List<User> users = new ArrayList<>();
        String query = String.format(
                "SELECT * FROM %s JOIN users_roles ON users.user_id = users_roles.user_id "
                        + "JOIN roles ON users_roles.role_id = roles.role_id;", TABLE_USERS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                User user = new User();
                setUsersFields(rs, user);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't get all users " + e);
        }
    }

    @Override
    public User update(User user) throws DataProcessingException {
        String query = String.format("UPDATE %s SET name =?, surname =?,"
                + " login =?, password =?, token =?, salt =? WHERE user_id =?;", TABLE_USERS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setString(5, user.getToken());
            statement.setBytes(6, user.getSalt());
            statement.setLong(7, user.getUserId());
            statement.executeUpdate();
            return user;
        } catch (SQLException e) {
            throw new DataProcessingException("Can't update user " + e);
        }
    }

    @Override
    public boolean deleteById(Long id) throws DataProcessingException {
        return delete(get(id).get());
    }

    @Override
    public boolean delete(User user) throws DataProcessingException {
        String query = String.format("DELETE FROM %s WHERE user_id =?;", TABLE_USERS);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, user.getUserId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataProcessingException("Can't delete user by id " + e);
        }
        return false;
    }
}
