package org.example.diary.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userDto) {
        userService.save(userDto);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> longUser(@RequestBody UserLoginDTO userLoginDto) {
        return userService.login(userLoginDto);
    }

    @GetMapping("/signup")
    public String signup(){
        return "user/signup";
    }


    @GetMapping("/login")
    public String login(){
        return "user/login";
    }
}