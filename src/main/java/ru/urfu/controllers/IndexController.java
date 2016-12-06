package ru.urfu.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.models.Message;
import ru.urfu.storage.MessagesStorage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class IndexController {
    @Inject @Named("messagesStorage")
    private MessagesStorage messagesStorage;
    private ObjectMapper mapper = new ObjectMapper();
	private final Log logger = LogFactory.getLog(getClass());

    @RequestMapping("/")
    public ModelAndView index(HttpServletResponse response) throws JsonProcessingException {
        List<Message> messages = messagesStorage.getAllMessages();
		response.addHeader("Content-Type", "text/html; charset=utf-8");
        return new ModelAndView("index", "messagesJson", mapper.writeValueAsString(messages));
    }
}
