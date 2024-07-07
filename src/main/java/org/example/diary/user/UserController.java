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
    @Autowired
    private HttpSession httpSession;

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegisterDTO userDto) {
        userService.save(userDto);
        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 잘못되었습니다.");
        }
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
        return "redirect:/diary/list";
    }

    @GetMapping("/updatePw")
    public String updatePw(Model model){
        Object email = httpSession.getAttribute("email");
        if(email != null){
            return "user/updatePw";
        }
        return "user/inpEmail";//이메일 인증 페이지
    }

    @PostMapping("/updatePw")
    public String updatePw(@Valid @ModelAttribute UpdatePwDTO userDto, Model model){
        if(!userDto.getPassword().equals(userDto.getConfirmPw())){
            System.out.println("불일치");
            model.addAttribute("errorMessage", "서로 일치하지 않습니다.");
            return "user/updatePw";
        }
        userService.updatePw(userDto);
        httpSession.removeAttribute("email");
        return "redirect:/diary/list";
    }

    @GetMapping("/findPw")
    public String findPw(){
        return "user/findPw";
    }

    @PostMapping("/sendCode")
    public String sendCode(@RequestParam String email, Model model) throws InterruptedException {
        int status = userService.sendCode(email);
        System.out.println(status);
        if(status == 0){
            model.addAttribute("errorMessage", "없는 이메일 잆니다.");
            return "user/inpEmail";
        }else{
            httpSession.setAttribute("email", email);
            return "user/inpCode";
        }
    }

    @PostMapping("/inpCode")
    public String inpCode(@RequestParam String code, Model model){
        if(code.isEmpty()){
            model.addAttribute("message", "코드를 입력해 주세요(12345)");
        }else if(!code.equals("12345")){
            model.addAttribute("message", "코드가 다릅니다. (12345)");
        }else if(code.equals("12345")){
            return "redirect:/user/updatePw";
        }
        model.addAttribute("getCode", 12345);
        return "user/inpCode";
    }
}