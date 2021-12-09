package com.example.social_network.UI;

import com.example.social_network.domain.validators.ValidationException;
import com.example.social_network.service.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UIadmin implements InterfaceUI{
    private final Service service;
    private final Scanner in;
    private final PrintStream out;

    /**
     * Constructor with parameters
     *
     * @param service - Service
     * @param in      - Scanner
     * @param out     - PrintStream
     */
    public UIadmin(Service service, InputStream in, PrintStream out) {
        this.service = service;
        this.in = new Scanner(in);
        this.out = out;
    }

    /**
     * Run menu
     */
    @Override
    public void runMenu() {
        char choice;

        showMenu();

        while (true) {
            out.println("\nInsert your choice and press ENTER: ");

            choice = in.next().charAt(0);

            switch (choice) {
                case '1' -> uiAddUser();
                case '2' -> uiDeleteUser();
                case '3' -> uiUpdateUser();
                case '4' -> uiAddFriendship();
                case '5' -> uiDeleteFriendship();
                case '6' -> uiNumberOfCommunities();
                case '7' -> uiLongestPath();
                case '8' -> uiShowFriendsForUser();
                case '9' -> uiShowFriendsForUserByMonth();
                case 'a' -> uiListAllUsers();
                case 'b' -> uiListAllFriendships();
                case 'x' -> {
                    return;
                }
                default -> out.println("Invalid choice!");
            }
        }
    }

    /**
     * Menu
     */
    private void showMenu() {
        out.println("----------------------------------------------");
        out.println("1. Add user.");
        out.println("2. Delete user.");
        out.println("3. Update user.");
        out.println("4. Add friend.");
        out.println("5. Remove friend.");
        out.println("6. Number of communities.");
        out.println("7. Longest path.");
        out.println("8. List all friends for user.");
        out.println("9. List all friends for user by month.");
        out.println("a. List all users.");
        out.println("b. List all friendships.");
        out.println("x. Back.");
        out.println("----------------------------------------------");
    }

    /**
     * Add user
     */
    private void uiAddUser() {
        String username;
        out.println("Username: ");
        username = in.next();

        try {
            service.addUser(username);
            out.println("User added!");
        } catch (ValidationException | IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }

    }

    /**
     * Delete user
     */
    private void uiDeleteUser() {
        String username;
        out.println("Username: ");
        username = in.next();

        try {
            service.deleteUser(username);
            out.println("User deleted!");
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    /**
     * Update user
     */
    private void uiUpdateUser() {
        String username, newUsername;
        out.println("Username: ");
        username = in.next();

        out.println("New username: ");
        newUsername = in.next();

        try {
            service.updateUser(username, newUsername);
            out.println("User updated!");
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    /**
     * Add friendship
     */
    private void uiAddFriendship() {
        String username1, username2;
        out.println("Username: ");
        username1 = in.next();

        out.println("Username: ");
        username2 = in.next();

        try {
            service.addFriendship(username1, username2);
            out.println("Friendship added");
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    /**
     * Delete friendship
     */
    private void uiDeleteFriendship() {
        String username1, username2;
        out.println("Username: ");
        username1 = in.next();

        out.println("Username: ");
        username2 = in.next();

        try {
            service.deleteFriendship(username1, username2);
            out.println("Friendship deleted");
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    /**
     * List all users
     */
    private void uiListAllUsers() {
        service.getAllUsers().forEach(out::println);
    }

    /**
     * List all friendships
     */
    private void uiListAllFriendships() {
        service.getAllFriendships().forEach(out::println);
    }

    /**
     * Show number of communities
     */
    private void uiNumberOfCommunities() {
        out.println("Number of communities: " + service.nrComponenteConexe());
    }

    /**
     * Show longest path
     */
    private void uiLongestPath() {
        ArrayList<String> longestPath = service.longestPath();
        out.println("Longest path is from " + longestPath.get(0) + " to " + longestPath.get(1) + " of length " + longestPath.get(2));
    }

    /**
     * Show friends for one user
     */
    private void uiShowFriendsForUser() {
        String name;
        out.println("Username:");
        name = in.next();
        try {
            List<String> friends = service.getAllFriendsForUser(name);
            if (friends.isEmpty())
                out.println(name + " has no friends! :( ");
            else
                friends.forEach(out::println);
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }

    /**
     * Show friends for one user from one month
     */
    private void uiShowFriendsForUserByMonth() {
        String name;
        out.println("Username: ");
        name = in.next();

        int month;
        out.println("Month: ");
        month = in.nextInt();

        while(month < 1 || month > 12) {
            out.println("Month must be in [1,12]\n");
            out.println("Month: ");
            month = in.nextInt();
        }

        try {
            List<String> friends = service.getAllFriendsForUserByMonth(name, month);
            if (friends.isEmpty())
                out.println(name + " did not make any friends in month " + String.valueOf(month) + "! :( ");
            else
                friends.forEach(out::println);
        } catch (IllegalArgumentException exception) {
            out.println(exception.getMessage());
        }
    }
}
