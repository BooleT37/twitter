package ru.urfu.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TBL_MESSAGE")
public class Message implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "content", length = 500, nullable = false)
    private String content;

	public Message() {}

	public Message(Long id, String content) {
		this.id = id;
		this.content = content;
	}

	@JsonCreator
    public Message(@JsonProperty("content") String content) {
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;
		return message.id == this.id;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(id);
	}
}
