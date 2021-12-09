package com.example.social_network.util;
import javafx.scene.control.Button;

public class FriendRequest {
    public final String username;
    public final String date;
    public final Button accept, reject;

    public FriendRequest(String username1, String date1){
        username = username1;
        date = date1;
        accept = new Button("Accept");
        reject = new Button("Reject");
    }

    public final String getUsername() {
        return username;
    }

    public final String getDate() {
        return date;
    }

    public final Button getAccept() {
        return accept;
    }

    public final Button getReject() {
        return reject;
    }


    @Override
    public String toString() {
        return "FriendRequest{" +
                "username='" + username + '\'' +
                ", date='" + date + '\'' +
                ", accept=" + accept +
                ", reject=" + reject +
                '}';
    }
}
