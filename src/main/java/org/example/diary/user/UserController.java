package org.example.diary.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/regist")
    public ResponseEntity<String> register(@RequestBody UserRegisterDTO userDto) {
        userService.save(userDto);
        return ResponseEntity.ok("success");
    }


    @PostMapping("/login")
    public ResponseEntity<String> longUser(@RequestBody UserLoginDTO userLoginDto) {
        return userService.login(userLoginDto);
    }
}
