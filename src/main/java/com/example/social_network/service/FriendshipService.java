package com.example.social_network.service;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.FriendshipStatus;
import com.example.social_network.domain.Tuple;
import com.example.social_network.repository.paging.Page;
import com.example.social_network.repository.paging.Pageable;
import com.example.social_network.repository.paging.PageableImplementation;
import com.example.social_network.repository.paging.PagingRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendshipService {
    PagingRepository<Tuple<Long, Long>, Friendship> repo;

    /**
     * Constructor with parameters
     *
     * @param repo - repository
     */
    public FriendshipService(PagingRepository<Tuple<Long, Long>, Friendship> repo) {
        this.repo = repo;
    }

    /**
     * Add friendship in repository
     *
     * @param user1 - ID of User1
     * @param user2 - ID of User2
     */
    public void addFriendship(Long user1, Long user2) {
        Friendship friendship = new Friendship(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FriendshipStatus.PENDING);
        Tuple<Long, Long> tuple = new Tuple<>(user1, user2);
        friendship.setId(tuple);

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

    public Set<Friendship> getFriendshipsOnPage(int page) {
        Pageable pageable = new PageableImplementation(page, 5);
        Page<Friendship> friendshipPage = repo.findAll(pageable);
        return friendshipPage.getContent().collect(Collectors.toSet());
    }
}
