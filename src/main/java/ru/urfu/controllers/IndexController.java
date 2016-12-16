package ru.urfu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.entities.Message;
import ru.urfu.entities.User;
import ru.urfu.storage.messages.MessagesStorage;
import ru.urfu.storage.users.UsersStorage;
import ru.urfu.storage.users.exceptions.UserAlreadyExists;
import ru.urfu.storage.users.exceptions.UserNotFound;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class IndexController {
    @Inject @Named("messagesStorage")
    private MessagesStorage messagesStorage;
    @Inject @Named("usersStorage")
	private UsersStorage usersStorage;
    private ObjectMapper mapper = new ObjectMapper();
	private final Log logger = LogFactory.getLog(getClass());

    @RequestMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("USER")))
            response.sendRedirect("/messages/" + auth.getName());
        else
			response.sendRedirect("/login");
    }

	@GetMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@GetMapping("/signup")
	public String signup(HttpServletRequest request, Model model) {
		String error = request.getParameter("error");
		if (error != null && !error.isEmpty()) {
			String errorMessage; //todo печатать сообщение прямо с e.getMessage()
			switch (error) {
				case "passwordsDiffer":
					errorMessage = "Пароли отличаются";
					break;
				case "userAlreadyExists":
					String username = request.getParameter("username");
					errorMessage = String.format("Пользователь '%s' уже существует", username);
					break;
				default: {
					errorMessage = error;
				}
			}
			model.addAttribute("errorMessage", errorMessage);
		}
		return "signup";
	}

	@PostMapping("/signup")
	public void signup(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String login = request.getParameter("username");
		String password = request.getParameter("password");
		if (!Objects.equals(password, request.getParameter("repeatPassword"))) {
			response.sendRedirect("/signup?error=passwordsDiffer");
			return;
		}
		User user = new User(login, password);
		try {
			usersStorage.add(user);
		} catch (UserAlreadyExists userAlreadyExists) {
			response.sendRedirect("/signup?error=userAlreadyExists&username=" + login);
			return;
		}
		response.sendRedirect("/login?newUser=" + login);
	}

	@RequestMapping("/messages/{username}")
	public ModelAndView index(@PathVariable("username") String username, HttpServletResponse response) throws JsonProcessingException {
		User user;
		try {
			user = usersStorage.getByLogin(username);
		} catch (UserNotFound userNotFound) {
			throw new ResourceNotFoundException(userNotFound.getMessage());
		}
		List<Message> messages = messagesStorage.getAll(user);
		response.addHeader("Content-Type", "text/html; charset=utf-8");
		Map<String, Object> model = new HashMap<>();
		Map<String, Object> jsData = new HashMap<>();
		jsData.put("messages", messages);
		jsData.put("userLogin", user.getLogin());

		model.put("jsonData", mapper.writeValueAsString(jsData));
		model.put("userLogin", user.getLogin());
		return new ModelAndView("messages", "model", model);
	}
}
