package com.example.social_network.UI;

import com.example.social_network.domain.Message;
import com.example.social_network.domain.User;
import com.example.social_network.service.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class UIuser implements InterfaceUI {
    private final Service service;
    private final Scanner in;
    private final PrintStream out;
    private User currentUser;

    /**
     * Constructor with parameters
     *
     * @param service - Service
     * @param in      - Scanner
     * @param out     - PrintStream
     */
    public UIuser(Service service, InputStream in, PrintStream out) {
        this.service = service;
        this.in = new Scanner(in);
        this.out = out;
        currentUser = null;
    }

    /**
     * Run menu
     */
    @Override
    public void runMenu() {

        if (logIn()) {
            showMenu();
            char choice;
            while (true) {
                out.println("\nInsert your choice and press ENTER: ");

                choice = in.next().charAt(0);

                switch (choice) {
                    case '1' -> uiSendFriendRequest();
                    case '2' -> uiSendNewMessage();
                    case 'a' -> uiShowFriendRequests();
                    case 'b' -> uiShowConversationWithAnotherUser();
                    case 'x' -> {
                        currentUser = null;
                        return;
                    }
                    default -> out.println("Invalid choice!");
                }
            }
        }

    }

    /**
     * Menu
     */
    private void showMenu() {
        out.println("----------------------------------------------");
        out.println("1. Send friend request.");
        out.println("2. Send new message to another user.");
        out.println("a. Show friend requests.");
        out.println("b. Show conversation with another user.");
        out.println("x. Log out.");
        out.println("----------------------------------------------");
    }

    public boolean logIn() {
        out.println("You need to log in first: ");
        out.println("Username: ");
        String username = in.next();

        if (service.getUser(username) != null) {
            currentUser = service.getUser(username);
            return true;
        } else {
            out.println("Incorrect username!\n");
            return false;
        }
    }

    public void uiSendFriendRequest() {
        out.println("Username: ");
        String username = in.next();
        try {
            service.addFriendship(currentUser.getUsername(), username);
            out.println("Friend request sent!");
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    public void uiChangeStatusFriendRequest(String username) {
        out.println("Accept/Reject (a/r) ");
        out.println("Press s for skip");

        String choice = in.next();
        switch (choice) {
            case "a" -> {
                service.updateFriendshipStatus(currentUser.getUsername(), username, "APPROVED");
                out.println("Friend request approved!");
            }
            case "r" -> {
                service.updateFriendshipStatus(currentUser.getUsername(), username, "REJECTED");
                out.println("Friend request rejected!");
            }
            case "s" -> out.println("Skipped! -_-");
            default -> out.println("Invalid choice");
        }
    }

    public void uiShowFriendRequests() {
        if (!service.getAllPendingFriendships(currentUser.getUsername()).isEmpty()) {
            out.println("Friend requests:");
            for (String friendRequest : service.getAllPendingFriendships(currentUser.getUsername())) {
                out.println(friendRequest);
                String username = friendRequest.split(" ")[0].trim();
                uiChangeStatusFriendRequest(username);
            }
        } else
            out.println("You don't have any friend request!");
    }

    public void uiSendNewMessage() {
        out.println("To: (use ; for more than one user)");
        String to = in.next();

        in.nextLine();

        out.println("Message: ");
        String message = in.nextLine();

        try {
            service.sendMessage(currentUser.getUsername(), to, message, 0L);
            out.println("Message sent!");
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    public void uiReply(String username) {
        boolean replyAll = true;
        int noConversations = service.getConversations(currentUser.getUsername(), username).size();
        out.println("Reply to conversation no. ...");
        int conversationNo = in.nextInt();
        while (conversationNo < 1 || conversationNo > noConversations) {
            out.println("Incorrect no. of conversation.");
            conversationNo = in.nextInt();
        }
        List<Message> conversation = service.getConversations(currentUser.getUsername(), username).get(conversationNo - 1);
        Message messageToReply = conversation.get(conversation.size() - 1);

        if (messageToReply.getTo().size() == 1)
            replyAll = false;
        else {
            out.println("1. Reply to " + username);
            out.println("2. Reply all");
            out.println("Choice = ");
            String option = in.next();
            while (!Objects.equals(option, "1") && !Objects.equals(option, "2")) {
                out.println("Invalid choice!");
                out.println("Choice = ");
                option = in.next();
            }
            if (option.equals("1")) {
                replyAll = false;
            }
        }

        in.nextLine();
        out.println("Message: ");
        String message = in.nextLine();


        StringBuilder to = new StringBuilder();
        to.append(messageToReply.getFrom().getUsername());
        if (replyAll) {
            for (var user : messageToReply.getTo()) {
                if (!Objects.equals(user.getUsername(), currentUser.getUsername()))
                    to.append(";").append(user.getUsername());
            }
        }

        if (!Objects.equals(messageToReply.getFrom().getUsername(), currentUser.getUsername())) {
            try {
                service.sendMessage(currentUser.getUsername(), to.toString(), message, messageToReply.getId());
                out.println("Reply sent!");
            } catch (IllegalArgumentException exception) {
                out.println(exception);
            }
        } else {
            out.println("Can't reply to this conversation.");
        }
    }

    public void uiShowConversationWithAnotherUser() {
        out.println("Username: ");
        String username = in.next();

        try {
            int numberOfConversations = 1;
            for (var conversation : service.getConversations(currentUser.getUsername(), username)) {
                out.println("\nConversation " + numberOfConversations++);
                for (var message : conversation) {
                    out.println(message);
                }
                out.println("------------------------------------------------");
            }

            if (numberOfConversations > 1) {
                out.println("\nDo you want to reply? (y/n)");
                String choice = in.next();
                if (Objects.equals(choice, "y")) {
                    uiReply(username);
                }
            } else {
                out.println("No conversation with " + username);
            }


        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }
}
