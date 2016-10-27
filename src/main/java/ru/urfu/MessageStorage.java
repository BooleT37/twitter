package ru.urfu;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author aarkaev
 * @since 08.08.2016
 */
@RestController
public class MessageStorage {
    @RequestMapping("/messages")
    String renderAllMessages() {
        MessageManager messageManager = new LocalMessageManager();
        String messages = messageManager.getAll().stream()
                .map(msg -> "<li>" + msg.getContent() + "</li>")
                .collect(Collectors.joining());

        return
            "<html>" +
            "   <link rel=\"stylesheet\" type=\"text/css\" href=\"/twitter.css\"/>" +
            "   <body>" +
            "       <h1>twitter</h1>" +
            "       This is your twitter application" +
            "       <ul class=\"messages\">" +
                        messages +
            "       </ul>"+
            "   </body>" +
            "</html>";
    }
}
