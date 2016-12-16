package ru.urfu;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import ru.urfu.storage.users.UsersStorage;

import javax.inject.Inject;
import javax.inject.Named;

@SpringBootApplication
public class TwitterApplication extends SpringBootServletInitializer {
    @Inject
    @Named("usersStorage")
    private UsersStorage usersStorage;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Application.class);
	}
	public static void main(String[] args) {
		SpringApplication.run(TwitterApplication.class, args);
	}

	/*@Bean
	public CommandLineRunner runner() {
		return args -> {
			User user = new User("admin", "password");
            usersStorage.add(user);
		};
	}*/
}