package io.javabrains.resumeportal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "hello";
    }

    @GetMapping("/edit")
    public String edit(){
        return "edit page";
    }
}
