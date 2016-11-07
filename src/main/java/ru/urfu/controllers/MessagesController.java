package ru.urfu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestOperations;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.models.Message;

import java.util.Map;

@Controller
public class MessagesController {
    @Autowired
    private RestOperations operations;

    @RequestMapping("/messages")
    public ModelAndView messages() {
        Map<Long, Message> messages = operations.getForObject("/getAllMessages", Map.class);
        return new ModelAndView("messages", "messages", messages);
    }
}
