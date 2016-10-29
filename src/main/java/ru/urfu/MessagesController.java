package ru.urfu;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.urfu.message.LocalMessageManager;
import ru.urfu.message.Message;
import ru.urfu.message.MessageManager;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author aarkaev
 * @since 08.08.2016
 */
@Controller
public class MessagesController {
    @RequestMapping("/messages")
    public ModelAndView messages() {
        MessageManager messageManager = new LocalMessageManager();
        List<Message> messages = messageManager.getAll();
        return new ModelAndView("messages", "messagesList", messages);
    }

    private List<String> getList() {

        List<String> list = new ArrayList<>();
        list.add("List A");
        list.add("List B");
        list.add("List C");
        list.add("List D");
        list.add("List 1");
        list.add("List 2");
        list.add("List 3");

        return list;

    }

    @RequestMapping(value = "/addMessage", method = POST)
    public void addMessage(@RequestParam(value = "content") String content) {
        MessageManager messageManager = new LocalMessageManager();
        messageManager.add(content);
    }
}