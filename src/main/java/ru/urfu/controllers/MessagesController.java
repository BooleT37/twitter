package ru.urfu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.models.Message;
import ru.urfu.storage.Storage;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class MessagesController {

    @Inject
    private Storage storage;

    @RequestMapping("/messages")
    public ModelAndView messages(HttpServletResponse response) {
        Map<Long, Message> messages = storage.getAllMessages();
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        return new ModelAndView("messages", "messages", messages);
    }
}
