package ru.urfu.models;

import javax.persistence.*;

@Entity
@Table(name = "TBL_MESSAGE")
public class User {
	@Id
	@Column(name = "login", length = 500, nullable = false)
	private String login;

	@Column(name = "password", length = 32, nullable = false)
	private String password;

	public User(String login, String password) {
		this.login = login;
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
