package com.siwuxie095.spring.chapter7th.example8th.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jiajing Li
 * @date 2021-02-05 20:51:59
 */
@SuppressWarnings("all")
@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

    @RequestMapping(method = RequestMethod.GET)
    public String uploadForm() {
        return "uploadForm";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processUpload(@RequestPart("file") MultipartFile file) {

        System.out.println(file.getSize());

//    System.out.println("---->  " + file.getName() + "  ::  "  + file.getSize());

        return "redirect:/";
    }

}
