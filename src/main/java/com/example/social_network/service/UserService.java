package com.example.social_network.service;

import com.example.social_network.domain.User;
import com.example.social_network.repository.Repository;

import java.util.Objects;

public class UserService {
    private final Repository<Long, User> repo;
    private Long id = 1L + (long) (Math.random() * (1000000L - 1L));

    /**
     * Constructor with parameter
     * @param repo - repository
     */
    public UserService(Repository<Long, User> repo) {
        this.repo = repo;
    }

    /**
     * Get repository
     * @return repository
     */
    public Repository<Long, User> getRepo() {
        return repo;
    }

    /**
     * Add user in repository
     * @param user - User to be added
     */
    public void addUser(User user) {
        id++;
        while(getUser(id) != null){
            id++;
        }
        user.setId(id);
        repo.save(user);
    }
    /**
     * Delete user from repository
     * @param username - username of User
     */
    public void deleteUser(String username) {
        repo.delete(getID(username));
    }

    /**
     * Update user with id = id
     * @param id -
     * @param user -
     */
    public void updateUser(Long id, User user){
        repo.update(id, user);
    }

    /**
     * Add friend in friend list
     * @param username1 - username of User1
     * @param username2 - username of User2
     */
    public void addFriend(String username1, String username2) {
        User user1 = getUser(username1);
        User user2 = getUser(username2);

        if (user1 == null || user2 == null)
            throw new IllegalArgumentException("Entity does not exist!");

        user1.getFriends().add(user2);
        user2.getFriends().add(user1);
    }

    /**
     * Get ID of User
     * @param username - username of User
     * @return - Long
     */
    public Long getID(String username) {
        for (User user : getAll()) {
            if (Objects.equals(user.getUsername(), username))
                return user.getId();
        }
        return null;
    }

    /**
     * Get User by ID
     * @param id - id of User
     * @return - User
     */
    public User getUser(Long id) {
        return repo.findOne(id);
    }

    /**
     * Get User by username
     * @param username - username of User
     * @return - User
     */
    public User getUser(String username) {
        for (User user : getAll()) {
            if (Objects.equals(user.getUsername(), username))
                return user;
        }
        return null;
    }

    /**
     * Get all users
     * @return List of users
     */
    public Iterable<User> getAll() {
        return repo.findAll();
    }
}
