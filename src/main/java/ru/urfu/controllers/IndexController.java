package ru.urfu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.urfu.logging.LoggingHelper;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {
    @Inject
    private LoggingHelper loggingHelper;

    @RequestMapping("/")
    public String index(HttpServletResponse response) {
		loggingHelper.helpMethod();
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        return "index";
    }
}
