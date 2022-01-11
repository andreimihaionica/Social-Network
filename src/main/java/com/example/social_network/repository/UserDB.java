package com.example.social_network.repository;

import com.example.social_network.domain.User;
import com.example.social_network.domain.validators.Validator;
import com.example.social_network.repository.paging.Page;
import com.example.social_network.repository.paging.Pageable;
import com.example.social_network.repository.paging.Paginator;
import com.example.social_network.repository.paging.PagingRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class UserDB implements PagingRepository<Long, User> {
    private String url, username, password;
    private Validator<User> validator;

    public UserDB(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(Long id) {
        if (id == null)
            throw new IllegalArgumentException("Entity does not exist!");

        for (User user : findAll())
            if (Objects.equals(user.getId(), id))
                return user;
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Users\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String username = resultSet.getString("username");

                User utilizator = new User(username);
                utilizator.setId(id);
                validator.validate(utilizator);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        if (findOne(entity.getId()) != null)
            throw new IllegalArgumentException("User already exists!");

        validator.validate(entity);
        String sql = "insert into \"Users\" (username) values (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long id) {
        if (findOne(id) == null)
            throw new IllegalArgumentException("User does not exist!");

        String sql = "delete from \"Users\" where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User update(Long id, User entity) {
        if (findOne(id) == null)
            throw new IllegalArgumentException("User does not exist!");

        validator.validate(entity);

        String sql = "update \"Users\" set username = (?) where id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getUsername());
            ps.setLong(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Paginator<User> paginator = new Paginator<>(pageable, this.findAll());
        return paginator.paginate();
    }
}
