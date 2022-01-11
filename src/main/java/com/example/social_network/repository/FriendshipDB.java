package com.example.social_network.repository;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.FriendshipStatus;
import com.example.social_network.domain.Tuple;
import com.example.social_network.domain.validators.Validator;
import com.example.social_network.repository.paging.Page;
import com.example.social_network.repository.paging.Pageable;
import com.example.social_network.repository.paging.Paginator;
import com.example.social_network.repository.paging.PagingRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

import static com.example.social_network.domain.FriendshipStatus.*;

public class FriendshipDB implements PagingRepository<Tuple<Long, Long>, Friendship> {
    private String url, username, password;
    Validator<Friendship> validator;

    public FriendshipDB(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> id) {
        if (id == null)
            throw new IllegalArgumentException("Entity does not exist!");

        String sql = "SELECT * from \"Friendships\" WHERE id1 = (?) and id2 = (?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id.getLeft());
            ps.setLong(2, id.getRight());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");

                FriendshipStatus friendshipStatus = switch (status) {
                    case "APPROVED" -> APPROVED;
                    case "REJECTED" -> REJECTED;
                    case "PENDING" -> PENDING;
                    default -> null;
                };

                Friendship friendship = new Friendship(date, friendshipStatus);
                friendship.setId(id);

                return friendship;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from \"Friendships\"");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                String date = resultSet.getString("date");
                String status = resultSet.getString("status");
                FriendshipStatus friendshipStatus;
                if (status.toUpperCase().equals("APPROVED"))
                    friendshipStatus = APPROVED;
                else if (status.toUpperCase().equals("REJECTED"))
                    friendshipStatus = REJECTED;
                else
                    friendshipStatus = PENDING;

                Friendship friendship = new Friendship(date, friendshipStatus);
                friendship.setId(new Tuple<>(id1, id2));
                friendships.add(friendship);
            }
            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        Tuple<Long, Long> id = new Tuple<>(entity.getId().getLeft(), entity.getId().getRight());
        if(findOne(id) != null)
            throw new IllegalArgumentException("Friendship already exists!");

        id.setLeft(entity.getId().getRight());
        id.setRight(entity.getId().getLeft());
        if(findOne(id) != null)
            throw new IllegalArgumentException("Friendship already exists!");

        String sql = "insert into \"Friendships\" (id1, id2, date, status) values (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, entity.getId().getLeft());
            ps.setLong(2, entity.getId().getRight());
            ps.setString(3, entity.getDate());
            ps.setString(4, entity.getStatus().toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> id) {
        if(findOne(id) == null)
            throw new IllegalArgumentException("Friendship does not exist!");

        String sql = "delete from \"Friendships\" where id1=(?) and id2=(?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id.getLeft());
            ps.setLong(2, id.getRight());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship update(Tuple<Long, Long> id, Friendship entity) {
        if(findOne(id) == null)
            throw new IllegalArgumentException ("Friendship does not exist!");

        validator.validate(entity);

        String sql = "update \"Friendships\" set status = (?), date = (?) where id1 = (?) and id2 = (?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getStatus().toString());
            ps.setString(2, entity.getDate());
            ps.setLong(3, id.getLeft());
            ps.setLong(4, id.getRight());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Page<Friendship> findAll(Pageable pageable) {
        Paginator<Friendship> paginator = new Paginator<>(pageable, this.findAll());
        return paginator.paginate();
    }
}
