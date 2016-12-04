package ru.urfu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.urfu.storage.JpaMessagesStorage;
import ru.urfu.storage.MessagesStorage;
import ru.urfu.storage.TemporalMessagesStorage;

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
}
