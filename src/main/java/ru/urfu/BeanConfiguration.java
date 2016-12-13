package ru.urfu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.urfu.storage.messages.JpaMessagesStorage;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.messages.TemporalMessagesStorage;
import ru.urfu.storage.users.JpaUsersStorage;
import ru.urfu.storage.users.TemporalUsersStorage;
import ru.urfu.storage.users.UsersStorage;

@Configuration
public class BeanConfiguration {
	@Value("${storage.type}")
	private String name;

	@Bean
	public MessagesStorage messagesStorage() {
		if (name.equals("db"))
			return new JpaMessagesStorage();
		return new TemporalMessagesStorage();
	}

	@Bean
	public UsersStorage usersStorage() {
		if (name.equals("db"))
			return new JpaUsersStorage();
		return new TemporalUsersStorage();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		if (name.equals("db"))
			return new JpaUsersStorage();
		return new TemporalUsersStorage();
	}
}
