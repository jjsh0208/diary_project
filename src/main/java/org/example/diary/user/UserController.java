package org.example.diary.user;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/regist")
    public String register(@RequestBody UserRegisterDTO userDto) {
        userService.save(userDto);
        return "redirect:/user/login";
    }

    @PostMapping("/login")
    public String login(@Valid UserLoginDTO userLoginDto) {
        userService.login(userLoginDto); // <- 여기 안에서 계저 조회하고 이메일 비번 동일시 시큐리티콘텐트 객체 생성
        return "/";
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