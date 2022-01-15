package com.example.social_network.repository;

import com.example.social_network.domain.Password;
import com.example.social_network.domain.validators.Validator;

import java.sql.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PasswordDB implements Repository<Long, Password> {
    private final String url;
    private final String username;
    private final String password;
    Validator<Password> validator;

    public PasswordDB(String url, String username, String password, Validator<Password> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Password findOne(Long aLong) {
        for (Password password : findAll())
            if (Objects.equals(password.getUserId(), aLong))
                return password;
        return null;
    }

    @Override
    public Iterable<Password> findAll() {
        Set<Password> passwords = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Passwords\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long userid = resultSet.getLong("userId");
                String passwordString = resultSet.getString("password");

                Password password = new Password(userid);
                password.setPassword(passwordString);
                validator.validate(password);
                passwords.add(password);
            }
            return passwords;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return passwords;
    }

    @Override
    public Password save(Password entity) {
        validator.validate(entity);
        String sql = "insert into \"Passwords\" (\"userId\", password) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, entity.getUserId());
            ps.setString(2, entity.getPassword());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Password delete(Long aLong) {
        return null;
    }

    @Override
    public Password update(Long aLong, Password entity) {
        return null;
    }
}
