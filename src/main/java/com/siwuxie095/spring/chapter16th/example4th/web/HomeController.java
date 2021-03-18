package com.siwuxie095.spring.chapter16th.example4th.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * @author Jiajing Li
 * @date 2021-03-16 22:37:52
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
