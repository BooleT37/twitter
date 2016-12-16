package ru.urfu.storage.users;

import ru.urfu.entities.User;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

public interface UsersStorage {
	User getByLogin(String login) throws UserNotFound;

	User add(User user) throws UserAlreadyExists;

	User deleteByLogin(String login) throws UserNotFound;
}
