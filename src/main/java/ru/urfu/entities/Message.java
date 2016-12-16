package ru.urfu.entities;

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

    @JoinColumn(name = "user")
    @ManyToOne(targetEntity=User.class)
    private User user;

	public Message() {}

	public Message(Long id, String content, User user) {
		this.id = id;
		this.content = content;
		this.user = user;
	}

	public Message(String content, User user) {
		this.content = content;
		this.user = user;
	}

	/*@JsonCreator
    public Message(@JsonProperty("content") String content, @JsonProperty("user") String user) {
        this.content = content;
        this.user = user;
    }*/

    public String getContent() {
        return this.content;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
