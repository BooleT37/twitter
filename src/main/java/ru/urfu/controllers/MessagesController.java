package ru.urfu.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.models.Message;
import ru.urfu.storage.TemporalStorage;
import ru.urfu.storageManager.IStorageManager;
import ru.urfu.storageManager.TemporalStorageManager;
import ru.urfu.storageManager.exceptions.StorageManagerException;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author aarkaev
 * @since 08.08.2016
 */
@Controller
public class MessagesController {

    private final TemporalStorage storage = TemporalStorage.getInstance();
    private final IStorageManager storageManager = new TemporalStorageManager(storage);


    @RequestMapping("/messages")
    public ModelAndView messages() {
        Map<Long, Message> messages = storageManager.getAllMessages();
        return new ModelAndView("messages", "messagesList", messages);
    }

    @RequestMapping(value = "/addMessage", method = POST)
    public void addMessage(@RequestParam(value = "content") String content, HttpServletResponse response) {
        try {
            storageManager.addMessageWithUniqId(new Message(content));
        } catch (StorageManagerException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            e.printStackTrace();
        }
    }
}