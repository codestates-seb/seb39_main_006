package com.codestates.seb006main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "/index.html";
    }
}
