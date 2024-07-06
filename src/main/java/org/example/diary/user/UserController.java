package org.example.diary.user;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/profile")
    public String profile(Model model){
        User user = userService.profile().get();
        model.addAttribute("user", user);
        return "user/profile";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute UserUpdateDTO userDto){
        userService.update(userDto);
        return "user/profile";
    }

    @GetMapping("/updatePw")
    public String updatePw(){
        return "user/updatePw";
    }

}