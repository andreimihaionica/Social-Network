package com.example.social_network.service;

import com.example.social_network.domain.Message;
import com.example.social_network.domain.User;
import com.example.social_network.repository.paging.Page;
import com.example.social_network.repository.paging.Pageable;
import com.example.social_network.repository.paging.PageableImplementation;
import com.example.social_network.repository.paging.PagingRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class MessageService {
    PagingRepository<Long, Message> repo;

    /**
     * Constructor with parameters
     *
     * @param repo - repository
     */
    public MessageService(PagingRepository<Long, Message> repo) {
        this.repo = repo;
    }

    /**
     * Get Message by ID
     *
     * @param id - id of Message
     * @return - Message
     */
    public Message getMessage(Long id) {
        return repo.findOne(id);
    }

    /**
     * Get all messages
     *
     * @return List of messages
     */
    public Iterable<Message> getAll() {
        return repo.findAll();
    }

    /**
     * Get reply for message with id, from, to
     *
     * @param id   - id of message
     * @param from - from of message
     * @param to   - to of message
     * @return Message
     */
    public Message getReply(Long id, String from, String to) {
        for (Message message : repo.findAll()) {
            if (Objects.equals(message.getReply(), id) &&
                    ((Objects.equals(message.getFrom().getUsername(), from) && message.getTo().toString().contains(to)) ||
                            (Objects.equals(message.getFrom().getUsername(), to) && message.getTo().toString().contains(from)) ||
                            (message.getTo().toString().contains(to) && message.getTo().toString().contains(from))))
                return message;
        }
        return null;
    }

    /**
     * Save message
     *
     * @param from    - User
     * @param to      - List of users
     * @param message - String
     * @param date    - LocalDateTime
     * @param reply   - id of reply
     */
    public Message sendMessage(User from, List<User> to, String message, LocalDateTime date, Long reply) {
        return repo.save(new Message(from, to, date, message, reply));
    }

    public Set<Message> getMessagesOnPage(int page) {
        Pageable pageable = new PageableImplementation(page, 5);
        Page<Message> messagePage = repo.findAll(pageable);
        return messagePage.getContent().collect(Collectors.toSet());
    }
}
