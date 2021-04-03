package com.siwuxie095.spring.chapter21th.example3rd.contact;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * @author Jiajing Li
 * @date 2021-04-03 10:55:36
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/")
public class ContactController {

    private ContactRepository contactRepo;

    @Autowired
    public ContactController(ContactRepository contactRepo) {
        this.contactRepo = contactRepo;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String home(Map<String, Object> model) {
        List<Contact> contacts = contactRepo.findAll();
        model.put("contacts", contacts);
        return "home";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(Contact contact) {
        contactRepo.save(contact);
        return "redirect:/";
    }

}

