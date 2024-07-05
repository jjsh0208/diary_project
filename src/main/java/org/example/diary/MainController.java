package org.example.diary;

import org.example.diary.user.User;
import org.example.diary.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String root (){
        return "redirect:/diary/list";
    }
}
