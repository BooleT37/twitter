package ru.urfu;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.urfu.models.User;
import ru.urfu.storage.users.UsersStorage;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Named
public class UserAuthenticationProvider implements AuthenticationProvider {
	@Inject @Named("usersStorage")
	private UsersStorage storage;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		User user;
		try {
			user = storage.getByLogin(name);
		} catch (UserNotFound userNotFound) {
			throw new UserAuthenticationException(userNotFound.getMessage());
		}
		if (!Objects.equals(user.getPassword(), password))
			throw new UserAuthenticationException("Wrong password");

		List<GrantedAuthority> grantedAuths = new ArrayList<>();
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
		return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return aClass.equals(UsernamePasswordAuthenticationToken.class);
	}

	class UserAuthenticationException extends AuthenticationException {
		public UserAuthenticationException(String msg, Throwable t) {
			super(msg, t);
		}

		UserAuthenticationException(String msg) {
			super(msg);
		}
	}
}
