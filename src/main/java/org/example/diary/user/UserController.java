package org.example.diary.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegisterDTO userDto) {
        userService.save(userDto);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "user/signup";
    }
}