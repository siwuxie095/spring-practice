package com.siwuxie095.spring.chapter8th.example4th;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Jiajing Li
 * @date 2021-02-10 18:55:33
 */
@SuppressWarnings("all")
@Controller
public class HomeController {

    @RequestMapping("/")
    public String redirectToFlow() {
        return "redirect:/pizza";
    }

}

