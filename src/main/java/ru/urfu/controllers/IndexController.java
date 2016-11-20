package ru.urfu.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(HttpServletResponse response) {
        response.addHeader("Content-Type", "text/html; charset=utf-8");
        return "index";
    }
}
