package com.example.social_network.service;

import com.example.social_network.domain.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PDFService {
    Service service;

    public PDFService(Service service) {
        this.service = service;
    }

    public List<Tuple<User, String>> getNewFriends(Date date1, Date date2, String username) {
        List<Tuple<User, String>> newFriends = new ArrayList<>();
        User user = service.getUser(username);
        for (Friendship friendship : service.getAllFriendships()) {
            try {
                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(friendship.getDate());
                if (date.after(date1) && date.before(date2) && friendship.getStatus() == FriendshipStatus.APPROVED) {
                    if (Objects.equals(friendship.getId().getLeft(), user.getId())) {
                        newFriends.add(new Tuple<>(service.getUser(friendship.getId().getRight()), friendship.getDate()));
                    } else if (Objects.equals(friendship.getId().getRight(), user.getId())) {
                        newFriends.add(new Tuple<>(service.getUser(friendship.getId().getLeft()), friendship.getDate()));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return newFriends;
    }

    public Integer noOfMessages(Date date1, Date date2, String username) {
        Iterable<Message> messages = service.getAllMessages();
        Integer contor = 0;
        for (Message message : messages) {
            Date date = Date.from(message.getDate().atZone(ZoneId.systemDefault()).toInstant());
            if (date.after(date1) && date.before(date2)) {
                for (User user : message.getTo()) {
                    if (Objects.equals(user.getUsername(), username)) {
                        contor++;
                        break;
                    }
                }
            }
        }
        return contor;
    }

    public List<Message> getMessages(Date date1, Date date2, String currentUsername, String username) {
        Iterable<Message> messages = service.getAllMessages();
        List<Message> messageList = new ArrayList<>();
        for (Message message : messages) {
            Date date = Date.from(message.getDate().atZone(ZoneId.systemDefault()).toInstant());
            if (date.after(date1) && date.before(date2) && Objects.equals(message.getFrom().getUsername(), username)) {
                for (User user : message.getTo()) {
                    if (Objects.equals(user.getUsername(), currentUsername)) {
                        messageList.add(message);
                        break;
                    }
                }
            }
        }
        return messageList;
    }

    public void statisticsPDF(Date date1, Date date2, String currentUsername, String username, String file) {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = null;
        try {
            contentStream = new PDPageContentStream(document, page);
            contentStream.setLeading(15f);
            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN), 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, 750);
            List<Tuple<User, String>> newFriends = getNewFriends(date1, date2, currentUsername);
            contentStream.showText(newFriends.size() + " new friends");
            contentStream.newLine();
            for (var newFriend : newFriends) {
                contentStream.showText(newFriend.getLeft().getUsername() + " " + newFriend.getRight());
                contentStream.newLine();
            }
            contentStream.newLine();
            contentStream.showText(noOfMessages(date1, date2, currentUsername) + " messages received");
            contentStream.newLine();
            contentStream.newLine();
            List<Message> messages = getMessages(date1, date2, currentUsername, username);
            if (messages.isEmpty()) {
                contentStream.showText("No messages from " + username + ".");
                contentStream.newLine();
            }
            contentStream.showText("Messages from " + username + ":");
            contentStream.newLine();
            for (Message message : messages) {
                contentStream.showText(message.toString());
                contentStream.newLine();
            }
            contentStream.endText();
            contentStream.close();

            document.save(file);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
