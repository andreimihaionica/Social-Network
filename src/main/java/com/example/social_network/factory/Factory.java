package com.example.social_network.factory;

import com.example.social_network.domain.Friendship;
import com.example.social_network.domain.Message;
import com.example.social_network.domain.Tuple;
import com.example.social_network.domain.User;
import com.example.social_network.domain.validators.FriendshipValidator;
import com.example.social_network.domain.validators.MessageValidator;
import com.example.social_network.domain.validators.UserValidator;
import com.example.social_network.domain.validators.Validator;
import com.example.social_network.repository.FriendshipDB;
import com.example.social_network.repository.MessageDB;
import com.example.social_network.repository.Repository;
import com.example.social_network.repository.UserDB;
import com.example.social_network.service.FriendshipService;
import com.example.social_network.service.MessageService;
import com.example.social_network.service.Service;
import com.example.social_network.service.UserService;

public class Factory {
    private UserService getUserService() {
        Validator<User> userValidator = new UserValidator();
        Repository<Long, User> userRepository = new UserDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", userValidator);
        //Repository<Long, User> userRepository = new UserDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "password", userValidator);
        return new UserService(userRepository);
    }

    private FriendshipService getFriendshipService() {
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", friendshipValidator);
        //Repository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "password", friendshipValidator);
        return new FriendshipService(friendshipRepository);
    }

    private MessageService getMessageService() {
        Validator<Message> messageValidator = new MessageValidator();
        Repository<Long, Message> messageRepository = new MessageDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", messageValidator);
        //Repository<Long, Message> messageRepository = new MessageDB("jdbc:postgresql://localhost:5432/postgres", "postgres", "password", messageValidator);
        return new MessageService(messageRepository);
    }

    public Service getService() {
        return new Service(getUserService(), getFriendshipService(), getMessageService());
    }
}
