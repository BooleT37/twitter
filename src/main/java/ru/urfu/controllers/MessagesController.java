package ru.urfu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.models.Message;
import ru.urfu.storageManager.StorageManager;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class MessagesController {

    @Inject
    private StorageManager storageManager;

    @RequestMapping("/messages")
    public ModelAndView messages() {
        Map<Long, Message> messages = storageManager.getAllMessages();
        return new ModelAndView("messages", "messages", messages);
    }
}
