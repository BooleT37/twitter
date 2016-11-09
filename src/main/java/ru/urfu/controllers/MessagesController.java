package ru.urfu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.models.Message;
import ru.urfu.storageManager.StorageManager;
import ru.urfu.storageManager.TemporalStorageManager;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class MessagesController {

    @Inject
    private StorageManager storageManager;

    @RequestMapping("/messages")
    public ModelAndView messages() {
        RestTemplate restTemplate = new RestTemplate();
        Map<Long, Message> messages = restTemplate.getForObject("http://localhost:8080/getAllMessages", Map.class);
        return new ModelAndView("messages", "messages", messages);
    }
}
