package com.example.social_network;

import com.example.social_network.factory.Factory;

import java.util.List;

/**
 * Main class
 */
public class Main {
    /**
     * Main
     * @param args -
     */
    public static void main(String[] args) {
        Factory factory = new Factory();
        factory.getUI().runMenu();
    }
}
