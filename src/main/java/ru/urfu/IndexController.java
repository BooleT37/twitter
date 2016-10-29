package ru.urfu;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.message.LocalMessageManager;
import ru.urfu.message.Message;
import ru.urfu.message.MessageManager;

import java.util.List;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(Model model) {
        return "index";
    }
}
