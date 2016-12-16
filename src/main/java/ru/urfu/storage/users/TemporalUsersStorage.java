package ru.urfu.storage.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.urfu.entities.User;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemporalUsersStorage implements UsersStorage, UserDetailsService {

	private Map<String, User> users = new HashMap<>();

	@Override
	public User getByLogin(String login) throws UserNotFound {
		User user = users.get(login);
		if (user == null)
			throw new UserNotFound(String.format("No user with name '%s'", login));
		return user;
	}

	@Override
	public User add(User user) throws UserAlreadyExists {
		if (users.containsKey(user.getLogin()))
			throw new UserAlreadyExists(String.format("User '%s' already exists", user.getLogin()));
		users.put(user.getLogin(), user);
		return user;
	}

	@Override
	public User deleteByLogin(String login) throws UserNotFound {
		if (!users.containsKey(login))
			throw new UserNotFound(String.format("No user with name '%s'", login));
		return users.remove(login);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user;
		try {
			user = this.getByLogin(username);
		} catch (UserNotFound userNotFound) {
			userNotFound.printStackTrace();
			throw new UsernameNotFoundException(userNotFound.getMessage());
		}
		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), grantedAuths);
	}
}
