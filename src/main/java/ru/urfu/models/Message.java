package ru.urfu.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class Message implements Serializable {
	private Long id;
    private final String content;

	@JsonCreator
    public Message(@JsonProperty("content") String content) {
        this.content = content;
    }

	public Message(Long id, String content) {
		this.id = id;
		this.content = content;
	}

    public String getContent() {
        return this.content;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
