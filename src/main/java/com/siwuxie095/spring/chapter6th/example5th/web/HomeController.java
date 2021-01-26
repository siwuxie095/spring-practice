package com.siwuxie095.spring.chapter6th.example5th.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Jiajing Li
 * @date 2021-01-26 23:04:11
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
