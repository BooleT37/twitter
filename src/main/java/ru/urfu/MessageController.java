package ru.urfu;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class MessageController {

    @RequestMapping(value = "/addMessage", method = POST)
    public void addMessage(@RequestParam(value = "content") String content) {
        MessageManager messageManager = new LocalMessageManager();
        messageManager.add(content);
    }

}
