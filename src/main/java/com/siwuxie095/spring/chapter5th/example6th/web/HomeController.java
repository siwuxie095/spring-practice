package com.siwuxie095.spring.chapter5th.example6th.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * HomeController：超级简单的控制器
 *
 * @author Jiajing Li
 * @date 2021-01-22 08:15:41
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping(method = RequestMethod.GET)
    public String home(Model model) {
        return "home";
    }

}
