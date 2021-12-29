package com.example.social_network.repository;

import com.example.social_network.domain.Message;
import com.example.social_network.domain.User;
import com.example.social_network.domain.validators.Validator;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class MessageDB implements Repository<Long, Message> {
    private String url, username, password;
    private Validator<Message> validator;

    public MessageDB(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("Message does not exist!");

        for (Message message : findAll())
            if (Objects.equals(message.getId(), aLong))
                return message;
        return null;
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Messages\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User userFrom = null;
                List<User> listOfUsersTo = new ArrayList<>();
                PreparedStatement statement1 = connection.prepareStatement("SELECT * from \"Users\" where username = (?)");
                statement1.setString(1, resultSet.getString(2));
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()) {
                    userFrom = new User(resultSet1.getString(2));
                    userFrom.setId(resultSet1.getLong(1));
                }

                String usersTo = resultSet.getString(3);
                String[] listOfUsers = usersTo.split(";");
                for (var user : listOfUsers) {
                    PreparedStatement statement2 = connection.prepareStatement("SELECT * from \"Users\" where username = (?)");
                    statement2.setString(1, user);
                    ResultSet resultSet2 = statement2.executeQuery();
                    User userTo = null;
                    while (resultSet2.next()) {
                        userTo = new User(resultSet2.getString(2));
                        userTo.setId(resultSet2.getLong(1));
                    }
                    listOfUsersTo.add(userTo);
                }

                String messageSend = resultSet.getString(4);
                LocalDateTime date = resultSet.getTimestamp(5).toLocalDateTime();
                Long reply = resultSet.getLong(6);
                Message message = new Message(userFrom, listOfUsersTo, date, messageSend, reply);
                message.setId(resultSet.getLong(1));
                validator.validate(message);
                messages.add(message);
            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        validator.validate(entity);
        String sql = "insert into \"Messages\" (\"from\", \"to\", message, date, reply) values (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getFrom().getUsername());
            String list = entity.getTo().stream().map(x -> x.getUsername()).reduce((x, y) -> (x + ";" + y)).get();
            ps.setString(2, list);
            ps.setString(3, entity.getMessage());
            ps.setTimestamp(4, Timestamp.valueOf(entity.getDate()));
            if (entity.getReply() != 0)
                ps.setLong(5, entity.getReply());
            else ps.setLong(5, 0);
            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Message delete(Long id) {
        if(findOne(id) == null)
            throw new IllegalArgumentException("Message does not exist!");

        String sql = "delete from \"Messages\" where id=(?)";

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
    public Message update(Long aLong, Message entity) {
        return null;
    }
}
