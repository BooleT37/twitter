package ru.urfu.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Message implements Serializable {
    private final String content;

	@JsonCreator
    public Message(@JsonProperty("content") String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }
}
