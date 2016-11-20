package ru.urfu.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    private final String content;

	@JsonCreator
    public Message(@JsonProperty("content") String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
