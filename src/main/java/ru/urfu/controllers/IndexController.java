package ru.urfu.controllers;

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

    @Inject
    @Named("jpaMessagesStorage")
    private MessagesStorage messagesStorage;

    @RequestMapping("/")
    public ModelAndView index(HttpServletResponse response) {
        List<Message> messages = messagesStorage.getAllMessages();
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        return new ModelAndView("index", "messages", messages);
    }
}
