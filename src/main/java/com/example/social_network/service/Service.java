package com.example.social_network.service;

import com.example.social_network.domain.*;
import com.example.social_network.repository.UserDB;
import com.example.social_network.util.Graph;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.social_network.domain.FriendshipStatus.*;
import static java.lang.String.valueOf;

/**
 * Service class
 */
public class Service {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final MessageService messageService;
    private final PasswordService passwordService;

    /**
     * Constructor with parameters
     *
     * @param userService       - UserService
     * @param friendshipService - FriendshipService
     */
    public Service(UserService userService, FriendshipService friendshipService, MessageService messageService, PasswordService passwordService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.messageService = messageService;
        this.passwordService = passwordService;

        if (userService.getRepo() instanceof UserDB)
            loadData();
    }

    /**
     * Add user in repository
     *
     * @param username - String
     */
    public void addUser(String username) {
        if (userService.getUser(username) == null) {
            User user = new User(username);
            userService.addUser(user);
        } else {
            throw new IllegalArgumentException("User already exists!");
        }
    }

    /**
     * Delete user from repository
     * Delete user from friendships
     * Delete user from other user friend list
     *
     * @param username - username of User
     */
    public void deleteUser(String username) {

        List<Tuple<Long, Long>> friendshipID = new ArrayList<>();

        for (Friendship friendship : friendshipService.getAll())
            if (Objects.equals(friendship.getId().getLeft(), userService.getID(username)) ||
                    Objects.equals(friendship.getId().getRight(), userService.getID(username)))
                friendshipID.add(friendship.getId());

        for (Tuple<Long, Long> id : friendshipID)
            friendshipService.deleteFriendship(id);

        for (User user : userService.getAll())
            user.getFriends().remove(userService.getUser(username));

        userService.deleteUser(username);
    }

    /**
     * Update user
     *
     * @param username    - String
     * @param newUsername - String
     */
    public void updateUser(String username, String newUsername) {
        if (userService.getUser(newUsername) == null) {
            if (userService.getUser(username) != null) {
                Long id = userService.getUser(username).getId();
                User user = new User(newUsername);
                user.setId(id);
                userService.updateUser(id, user);
            } else {
                throw new IllegalArgumentException("User does not exist!");
            }

        } else {
            throw new IllegalArgumentException("Username already exists!");
        }
    }

    /**
     * Get all users
     *
     * @return List of users
     */
    public Iterable<User> getAllUsers() {
        return userService.getAll();
    }

    public Set<User> getUsersOnPage(int page) {
        return userService.getUsersOnPage(page);
    }

    /**
     * Get User by ID
     *
     * @param id - id of User
     * @return - User
     */
    public User getUser(Long id) {
        return userService.getUser(id);
    }

    /**
     * Get User by username
     *
     * @param username - username of User
     * @return - User
     */
    public User getUser(String username) {
        return userService.getUser(username);
    }

    /**
     * Add friendship in repository
     * Add friend in user friend list
     *
     * @param username1 - username of User1
     * @param username2 - username of User2
     */
    public void addFriendship(String username1, String username2) {
        userService.addFriend(username1, username2);
        friendshipService.addFriendship(userService.getID(username1), userService.getID(username2));
    }

    /**
     * Delete friendship from repository
     * Delete friend from user friend list
     *
     * @param username1 - username of User1
     * @param username2 - username of User2
     */
    public void deleteFriendship(String username1, String username2) {
        for (User user : userService.getAll()) {
            if (Objects.equals(user.getUsername(), username1))
                user.getFriends().remove(userService.getUser(username2));
            if (Objects.equals(user.getUsername(), username2))
                user.getFriends().remove(userService.getUser(username1));
        }

        Tuple<Long, Long> tuple = null;
        for (Friendship friendship : friendshipService.getAll()) {
            if (Objects.equals(friendship.getId().getLeft(), userService.getID(username1)) &&
                    Objects.equals(friendship.getId().getRight(), userService.getID(username2)))
                tuple = new Tuple<>(userService.getID(username1), userService.getID(username2));

            if (Objects.equals(friendship.getId().getRight(), userService.getID(username1)) &&
                    Objects.equals(friendship.getId().getLeft(), userService.getID(username2)))
                tuple = new Tuple<>(userService.getID(username2), userService.getID(username1));

        }

        friendshipService.deleteFriendship(tuple);
    }

    public void updateFriendshipStatus(String username1, String username2, String status) {
        FriendshipStatus friendshipStatus;
        if (status.equals("APPROVED"))
            friendshipStatus = APPROVED;
        else if (status.equals("REJECTED"))
            friendshipStatus = REJECTED;
        else
            friendshipStatus = PENDING;
        Tuple<Long, Long> id = new Tuple<>(userService.getID(username2), userService.getID(username1));
        friendshipService.updateFriendshipStatus(id, friendshipStatus);
    }

    /**
     * Get all friendships
     *
     * @return List of friendships
     */
    public Iterable<Friendship> getAllFriendships() {
        return friendshipService.getAll();
    }

    public Set<Friendship> getFriendshipsOnPage(int page) {
        return friendshipService.getFriendshipsOnPage(page);
    }

    public FriendshipStatus verifyFriendship(String username1, String username2) {
        Tuple<Long, Long> id = new Tuple(userService.getUser(username1).getId(), userService.getUser(username2).getId());
        if (friendshipService.findOne(id) != null)
            return friendshipService.findOne(id).getStatus();

        id.setLeft(userService.getUser(username2).getId());
        id.setRight(userService.getUser(username1).getId());

        if (friendshipService.findOne(id) != null)
            return friendshipService.findOne(id).getStatus();

        return null;
    }

    public List<String> getAllPendingFriendships(String username) {
        User user = userService.getUser(username);
        List<String> friendships = new ArrayList<>();
        for (var friendship : friendshipService.getAll()) {
            if (friendship.getStatus() == PENDING && Objects.equals(friendship.getId().getRight(), user.getId()))
                friendships.add(userService.getUser(friendship.getId().getLeft()).getUsername() + " | " + friendship.getDate());
        }
        return friendships;
    }

    public Friendship getFriendship(String username1, String username2) {
        Friendship friendship = friendshipService.findOne(new Tuple<>(getUser(username1).getId(), getUser(username2).getId()));
        if (friendship == null) {
            return friendshipService.findOne(new Tuple<>(getUser(username2).getId(), getUser(username1).getId()));
        }
        return friendship;
    }

    /**
     * Determinate no. of connected components
     *
     * @return Integer
     */
    public int nrComponenteConexe() {
        int nrNoduri = 0;

        Map<Long, Integer> noduri = new HashMap<>();

        for (User user : userService.getAll())
            noduri.put(user.getId(), nrNoduri++);

        Graph g = new Graph(nrNoduri);
        Long nod1, nod2;
        for (Friendship friendship : friendshipService.getAll()) {
            if (friendship.getStatus() == APPROVED) {
                nod1 = friendship.getId().getLeft();
                nod2 = friendship.getId().getRight();

                g.addEdge(noduri.get(nod1), noduri.get(nod2));
            }
        }

        return g.ConnectedComponents();
    }

    /**
     * Determinate the longestPath
     *
     * @return List of String
     */
    public ArrayList<String> longestPath() {
        int nrNoduri = 0;

        Map<Long, Integer> noduri = new HashMap<>();

        for (User user : userService.getAll())
            noduri.put(user.getId(), nrNoduri++);

        Graph g = new Graph(nrNoduri);
        Long nod1, nod2;
        for (Friendship friendship : friendshipService.getAll()) {
            if (friendship.getStatus() == APPROVED) {
                nod1 = friendship.getId().getLeft();
                nod2 = friendship.getId().getRight();

                g.addEdge(noduri.get(nod1), noduri.get(nod2));
            }
        }

        ArrayList<Integer> path = g.longestPath();
        ArrayList<String> longestPath = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : noduri.entrySet()) {
            if (Objects.equals(entry.getValue(), path.get(0)))
                longestPath.add(userService.getUser(entry.getKey()).getUsername());

            if (Objects.equals(entry.getValue(), path.get(1)))
                longestPath.add(userService.getUser(entry.getKey()).getUsername());
        }
        longestPath.add(valueOf(path.get(2)));

        return longestPath;
    }

    /**
     * Load data
     */
    private void loadData() {
        String username1 = null, username2 = null;

        for (Friendship friendship : friendshipService.getAll()) {
            for (User user : userService.getAll()) {
                if (Objects.equals(user.getId(), friendship.getId().getLeft()))
                    username1 = user.getUsername();

                if (Objects.equals(user.getId(), friendship.getId().getRight()))
                    username2 = user.getUsername();
            }
            userService.addFriend(username1, username2);
        }
    }

    /**
     * Get all friends for one user
     *
     * @param username - String
     * @return list of string = username + date
     */
    public List<String> getAllFriendsForUser(String username) {
        User user = userService.getUser(username);
        if (user == null)
            throw new IllegalArgumentException("User does not exist!");
        List<Friendship> friendships = new ArrayList<>();
        friendshipService.getAll().forEach(friendships::add);
        return friendships.stream()
                .filter(x -> (Objects.equals(x.getId().getLeft(), user.getId()) || Objects.equals(x.getId().getRight(), user.getId())) && x.getStatus() == APPROVED)
                .map(x -> {
                    User friend;
                    if (Objects.equals(x.getId().getLeft(), user.getId()))
                        friend = userService.getUser(x.getId().getRight());
                    else
                        friend = userService.getUser(x.getId().getLeft());
                    return friend.getUsername() + " | " + x.getDate();
                })
                .collect(Collectors.toList());
    }

    /**
     * Get all friends for one user from one month
     *
     * @param username - String
     * @param month    - int
     * @return list of string = username + date
     */
    public List<String> getAllFriendsForUserByMonth(String username, int month) {
        User user = userService.getUser(username);
        if (user == null)
            throw new IllegalArgumentException("User does not exist!");
        List<Friendship> friendships = new ArrayList<>();
        friendshipService.getAll().forEach(friendships::add);
        return friendships.stream()
                .filter(x -> (Objects.equals(x.getId().getLeft(), user.getId()) || Objects.equals(x.getId().getRight(), user.getId())) &&
                        Integer.parseInt(x.getDate().substring(3, 5)) == month && x.getStatus() == APPROVED)
                .map(x -> {
                    User friend;
                    if (Objects.equals(x.getId().getLeft(), user.getId()))
                        friend = userService.getUser(x.getId().getRight());
                    else
                        friend = userService.getUser(x.getId().getLeft());
                    return friend.getUsername() + " | " + x.getDate();
                })
                .collect(Collectors.toList());
    }

    /**
     * Save message
     *
     * @param from    - String
     * @param to      - String
     * @param message - String
     * @param reply   - id of reply
     */
    public Message sendMessage(String from, String to, String message, Long reply) {
        User userFrom = null;
        if (userService.getUser(from) == null)
            throw new IllegalArgumentException("User " + from + " does not exist!");
        else
            userFrom = userService.getUser(from);

        StringBuilder errors = new StringBuilder("");
        List<String> usernames = List.of(to.split(";"));
        List<User> userTo = new ArrayList<>();
        for (String username : usernames) {
            if (userService.getUser(username) == null)
                errors.append("User ").append(username).append(" does not exist!\n");
            else
                userTo.add(userService.getUser(username));
        }

        LocalDateTime date = LocalDateTime.now();

        if (reply != 0 && messageService.getMessage(reply) == null)
            throw new IllegalArgumentException("Reply message does not exist!");

        if (!userTo.isEmpty())
            return messageService.sendMessage(userFrom, userTo, message, date, reply);

        if (errors.length() > 0)
            throw new IllegalArgumentException(errors.toString());

        return null;
    }

    /**
     * Get all conversations for two users
     *
     * @param from - username of user1
     * @param to   - username of user2
     * @return List of conversations
     */
    public List<List<Message>> getConversations(String from, String to) {
        if (userService.getUser(to) == null)
            throw new IllegalArgumentException("User " + to + " does not exist!");

        List<List<Message>> conversations = new ArrayList<>();
        List<Message> messages = new ArrayList<>();
        for (Message message : messageService.getAll()) {
            messages.add(message);
        }
        messages.sort(Comparator.comparing(Message::getDate));

        for (Message message : messages) {
            if (((Objects.equals(message.getFrom().getUsername(), from) && message.getTo().contains(getUser(to))) ||
                    (Objects.equals(message.getFrom().getUsername(), to) && message.getTo().contains(getUser(from))) ||
                    (message.getTo().contains(getUser(to)) && message.getTo().contains(getUser(from))))
                    && message.getReply() == 0L) {
                List<Message> conversation = new ArrayList<>();

                Message message1 = message;
                conversation.add(message1);

                while (messageService.getReply(message1.getId(), to, from) != null) {
                    message1 = messageService.getReply(message1.getId(), to, from);
                    conversation.add(message1);
                }

                conversations.add(conversation);
            }
        }
        return conversations;
    }

    public Iterable<Message> getAllMessages() {
        return messageService.getAll();
    }

    public Set<Message> getMessagesOnPage(int page) {
        return messageService.getMessagesOnPage(page);
    }

    public int getMutualFriends(String username1, String username2) {
        int mutualFriends = 0;

        for (Friendship friendship : getAllFriendships()) {

            if (Objects.equals(friendship.getId().getLeft(), getUser(username1).getId())) {

                if (getFriendship(getUser(friendship.getId().getRight()).getUsername(), username2) != null) {
                    mutualFriends++;
                }

            }

            if (Objects.equals(friendship.getId().getRight(), getUser(username1).getId())) {

                if (getFriendship(getUser(friendship.getId().getLeft()).getUsername(), username2) != null) {
                    mutualFriends++;
                }

            }

        }

        return mutualFriends;
    }

    public void addPassword(Long userId, String passwordString) {
        Password password = new Password(userId, passwordString);
        passwordService.addPassword(password);
    }

    public Password getPassword(Long userId) {
        return passwordService.findOne(userId);
    }
}
