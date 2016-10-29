package ru.urfu.message;

public class Message {
    private final long id;
    private String content;

    Message(long id, String content) {
        this.id = id;
        this.content = content;
    }

    long getId() {
        return this.id;
    }

    public String getContent() {
        return this.content;
    }
}
