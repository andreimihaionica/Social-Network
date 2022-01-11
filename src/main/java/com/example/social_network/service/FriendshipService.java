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
    private PagingRepository<Tuple<Long, Long>, Friendship> repo;

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

    public Set<Friendship> getFriendshipsOnPage(int page) {
        Pageable pageable = new PageableImplementation(page, 5);
        Page<Friendship> friendshipPage = repo.findAll(pageable);
        return friendshipPage.getContent().collect(Collectors.toSet());
    }
}
