package ru.urfu.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.urfu.models.Message;
import ru.urfu.storageManager.StorageManager;
import ru.urfu.storageManager.exceptions.StorageManagerException;
import ru.urfu.storageManager.exceptions.WrongIdException;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author aarkaev
 * @since 08.08.2016
 */
@RestController
public class MessagesRestController {

    @Inject
    private StorageManager storageManager;

    @RequestMapping(value = "/getAllMessages", method = GET)
    public Map<Long, Message> getAllMessages() {
        return storageManager.getAllMessages();
    }

    @RequestMapping(value = "/getMessage", method = GET)
    public ResponseEntity<Message> getMessage(@RequestParam("id") Long id) {
        try {
            return ResponseEntity.ok(storageManager.getMessageById(id));
        } catch (WrongIdException e) {
            System.out.printf("Trying to get message with id '%d', but no message with such id has been found\n", id);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/addMessage", method = POST)
    public ResponseEntity addMessage(@RequestBody String content) {
        try {
            storageManager.addMessageWithUniqId(new Message(content));
            return ResponseEntity.ok().build();
        } catch (StorageManagerException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
