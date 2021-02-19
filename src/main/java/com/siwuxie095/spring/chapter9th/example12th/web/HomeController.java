package com.siwuxie095.spring.chapter9th.example12th.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Jiajing Li
 * @date 2021-02-19 08:24:49
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(method = GET)
    public String home(Model model) {
        return "home";
    }

}
