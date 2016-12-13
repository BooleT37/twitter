package ru.urfu.storage.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import ru.urfu.models.User;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Named
public class JpaUsersStorage implements UsersStorage, UserDetailsService {
	@PersistenceContext
	private EntityManager em;

	@Override
	public User getByLogin(String login) throws UserNotFound {
		User user = em.find(User.class, login);
		if (user == null)
			throw new UserNotFound(String.format("No user with name '%s'", login));
		return user;
	}

	@Override
	@Transactional
	public User add(User user) throws UserAlreadyExists {
		User existingUser = em.find(User.class, user.getLogin());
		if (existingUser != null)
			throw new UserAlreadyExists(String.format("User '%s' already exists", user.getLogin()));
		em.persist(user);
		return user;
	}

	@Override
	public User deleteByLogin(String login) throws UserNotFound {
		User user = em.find(User.class, login);
		if (user == null)
			throw new UserNotFound(String.format("No user with name '%s'", login));
		em.remove(user);
		return user;
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
