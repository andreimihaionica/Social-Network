package com.example.social_network.service;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.FriendshipStatus;
import com.example.social_network.domain.Tuple;
import com.example.social_network.repository.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FriendshipService {
    private Repository<Tuple<Long, Long>, Friendship> repo;

    /**
     * Constructor with parameters
     *
     * @param repo - repository
     */
    public FriendshipService(Repository<Tuple<Long, Long>, Friendship> repo) {
        this.repo = repo;
    }

    /**
     * Add friendship in repository
     *
     * @param utilizator1 - ID of User1
     * @param utilizator2 - ID of User2
     */
    public void addFriendship(Long utilizator1, Long utilizator2) {
        Friendship friendship = new Friendship(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FriendshipStatus.PENDING);
        friendship.setId(new Tuple(utilizator1, utilizator2));

        repo.save(friendship);
    }

    /**
     * Delete friendship from repository
     *
     * @param tuple - ID of Friendship
     */
    public void deleteFriendship(Tuple<Long, Long> tuple) {
        repo.delete(tuple);
    }

    public void updateFriendshipStatus(Tuple<Long, Long> id, FriendshipStatus status) {
        Friendship newFriendship = new Friendship(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), status);
        repo.update(id, newFriendship);
    }

    public Friendship findOne(Tuple<Long, Long> id) {
        return repo.findOne(id);
    }

    /**
     * Get all friendships
     *
     * @return List of friendships
     */
    public Iterable<Friendship> getAll() {
        return repo.findAll();
    }
}
