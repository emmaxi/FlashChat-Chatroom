package com.emma.flashchat;

/**
 * Created by Emma on 1/16/18.
 */

public class InstantMessage {
    private String message;
    private String author;

    public InstantMessage(String message, String author) {
        this.message = message;
        this.author = author;
    }

    // required by Firebase API
    public InstantMessage() {
    }
    public String getMessage() {
        return message;
    }
    public String getAuthor() {
        return author;
    }
}
