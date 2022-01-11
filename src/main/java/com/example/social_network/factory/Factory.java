package com.example.social_network.factory;

import com.example.social_network.UI.UI;
import com.example.social_network.domain.*;
import com.example.social_network.domain.validators.*;
import com.example.social_network.repository.*;
import com.example.social_network.repository.paging.PagingRepository;
import com.example.social_network.service.*;

public class Factory {
    private UserService getUserService() {
        Validator<User> userValidator = new UserValidator();
        PagingRepository<Long, User> userRepository = new UserDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", userValidator);
        return new UserService(userRepository);
    }

    private FriendshipService getFriendshipService() {
        Validator<Friendship> friendshipValidator = new FriendshipValidator();
        PagingRepository<Tuple<Long, Long>, Friendship> friendshipRepository = new FriendshipDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", friendshipValidator);
        return new FriendshipService(friendshipRepository);
    }

    private MessageService getMessageService() {
        Validator<Message> messageValidator = new MessageValidator();
        PagingRepository<Long, Message> messageRepository = new MessageDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", messageValidator);
        return new MessageService(messageRepository);
    }

    private PasswordService getPasswordService() {
        Validator<Password> passwordValidator = new PasswordValidator();
        Repository<Long, Password> passwordRepository = new PasswordDB("jdbc:postgresql://localhost:5432/SocialNetwork", "postgres", "163202", passwordValidator);
        return new PasswordService(passwordRepository);
    }

    public Service getService() {
        return new Service(getUserService(), getFriendshipService(), getMessageService(), getPasswordService());
    }

    public UI getUI() {
        return new UI(getService(), System.in, System.out);
    }
}
