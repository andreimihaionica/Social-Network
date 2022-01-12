package com.example.social_network.repository;

import com.example.social_network.domain.Event;
import com.example.social_network.domain.User;
import com.example.social_network.domain.validators.Validator;
import com.example.social_network.repository.paging.Page;
import com.example.social_network.repository.paging.Pageable;
import com.example.social_network.repository.paging.Paginator;
import com.example.social_network.repository.paging.PagingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class EventDB implements PagingRepository<Long, Event> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<Event> validator;

    public EventDB(String url, String username, String password, Validator<Event> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Event findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("Event does not exist!");

        for (Event event : findAll())
            if (Objects.equals(event.getId(), aLong))
                return event;
        return null;
    }

    @Override
    public Iterable<Event> findAll() {
        Set<Event> events = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Events\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                User createdBy = null;
                List<User> listOfSubscribers = new ArrayList<>();
                PreparedStatement statement1 = connection.prepareStatement("SELECT * from \"Users\" where username = (?)");
                statement1.setString(1, resultSet.getString(2));
                ResultSet resultSet1 = statement1.executeQuery();
                while (resultSet1.next()) {
                    createdBy = new User(resultSet1.getString(2));
                    createdBy.setId(resultSet1.getLong(1));
                }

                String subscribers = resultSet.getString(8);
                String[] listOfUsersSubscribers = new String[0];
                if (subscribers != null)
                    listOfUsersSubscribers = subscribers.split(";");
                for (var user : listOfUsersSubscribers) {
                    PreparedStatement statement2 = connection.prepareStatement("SELECT * from \"Users\" where username = (?)");
                    statement2.setString(1, user);
                    ResultSet resultSet2 = statement2.executeQuery();
                    User subscriber = null;
                    while (resultSet2.next()) {
                        subscriber = new User(resultSet2.getString(2));
                        subscriber.setId(resultSet2.getLong(1));
                    }
                    listOfSubscribers.add(subscriber);
                }

                String name = resultSet.getString(3);
                String description = resultSet.getString(4);
                String location = resultSet.getString(5);
                String type = resultSet.getString(6);
                LocalDateTime date = resultSet.getTimestamp(7).toLocalDateTime();
                Event event = new Event(createdBy, name, description, location, type, date, listOfSubscribers);
                event.setId(resultSet.getLong(1));
                validator.validate(event);
                events.add(event);
            }
            return events;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    @Override
    public Event save(Event entity) {
        validator.validate(entity);
        String sql = "insert into \"Events\" (\"createdBy\", \"name\", description, location, type, date, subscribers) values (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getCreatedBy().getUsername());
            ps.setString(2, entity.getName());
            ps.setString(3, entity.getDescription());
            ps.setString(4, entity.getLocation());
            ps.setString(5, entity.getType());
            ps.setTimestamp(6, Timestamp.valueOf(entity.getDate()));
            String list = null;
            if (entity.getSubscribers().size() > 0)
                list = entity.getSubscribers().stream().map(User::getUsername).reduce((x, y) -> (x + ";" + y)).get();
            ps.setString(7, list);
            ps.executeUpdate();

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event delete(Long aLong) {
        if (findOne(aLong) == null)
            throw new IllegalArgumentException("Event does not exist!");

        String sql = "delete from \"Events\" where id=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Event update(Long aLong, Event entity) {
        if (findOne(aLong) == null)
            throw new IllegalArgumentException("Event does not exist!");

        validator.validate(entity);

        String sql = "update \"Events\" set subscribers = (?) where id = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            String list = null;
            if (entity.getSubscribers().size() > 0)
                list = entity.getSubscribers().stream().map(User::getUsername).reduce((x, y) -> (x + ";" + y)).get();
            ps.setString(1, list);
            ps.setLong(2, aLong);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        Paginator<Event> paginator = new Paginator<>(pageable, this.findAll());
        return paginator.paginate();
    }
}