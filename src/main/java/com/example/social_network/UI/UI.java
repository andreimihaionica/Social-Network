package com.example.social_network.UI;

import com.example.social_network.service.Service;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class UI implements InterfaceUI {
    private final Service service;
    private final Scanner in;
    private final PrintStream out;
    private UIadmin uiAdmin;
    private UIuser uiUser;

    /**
     * Constructor with parameters
     *
     * @param service - Service
     * @param in      - Scanner
     * @param out     - PrintStream
     */
    public UI(Service service, InputStream in, PrintStream out) {
        this.service = service;
        this.in = new Scanner(in);
        this.out = out;
        uiAdmin = new UIadmin(service, in, out);
        uiUser = new UIuser(service, in, out);
    }


    /**
     * Run menu
     */
    @Override
    public void runMenu() {
        char choice;
        while (true) {
            System.out.print("\033[H\033[2J");
            System.out.flush();
            showMenu();
            out.println("\nInsert your choice and press ENTER: ");

            choice = in.next().charAt(0);

            switch (choice) {
                case '1' -> uiAdmin.runMenu();
                case '2' -> uiUser.runMenu();
                case 'x' -> {
                    out.println("Bye, bye!");
                    System.exit(0);
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
        out.println("1. Admin menu.");
        out.println("2. User menu.");
        out.println("x. Exit.");
        out.println("----------------------------------------------");
    }
}
