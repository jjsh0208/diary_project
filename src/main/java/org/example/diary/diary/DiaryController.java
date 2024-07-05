package org.example.diary.diary;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.diary.Security.SecurityUtil;
import org.example.diary.user.User;
import org.example.diary.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    private final UserService userService;

    @GetMapping("/list")
    public String diaryList(Model model, HttpSession session){
        List<Diary> diaries = diaryService.getList();

        // 세션에 저장된 사용자 정보가 없을 경우에만 가져옴
        if (session.getAttribute("currentUser") == null) {
            User user = this.userService.getUser(SecurityUtil.getCurrentUsername());
            session.setAttribute("currentUser", user);
        }

        // 세션에서 사용자 정보를 가져와 모델에 추가
        User user = (User) session.getAttribute("currentUser");

        model.addAttribute("diaryList" , diaries);
        model.addAttribute("user",user);
        return "diary/index";
    }

    @GetMapping("/write")
    public String diaryCreate(DiaryFormDTO diaryForm ,Model model ,HttpSession session){

        // 세션에 저장된 사용자 정보가 없을 경우에만 가져옴
        if (session.getAttribute("currentUser") == null) {
            User user = this.userService.getUser(SecurityUtil.getCurrentUsername());
            session.setAttribute("currentUser", user);
        }

        // 세션에서 사용자 정보를 가져와 모델에 추가
        User user = (User) session.getAttribute("currentUser");

        model.addAttribute("user",user);


        return "diary/diaryForm";
    }

    @PostMapping("/write")

    public String diaryCreate(@Valid DiaryFormDTO diaryForm, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "diary/diaryForm";
        }

        // 파일 업로드 처리
        MultipartFile file = diaryForm.getImgFile();
        try {
            diaryService.diaryCreate(diaryForm.getSubject(),diaryForm.getContent(),
                    file,diaryForm.getMusic_url());
        }catch (IOException e){
            e.printStackTrace();
            return "diary/diaryForm";
        }

        return "redirect:/diary/list";
    }

    @GetMapping(value = "/show/{id}")

    public String show(Model model,@PathVariable("id") Long id){
        Diary diary1 = diaryService.getDiary(id);
        System.out.println(diary1.getImgFile());
        model.addAttribute(diary1);

        return  "diary/diaryShow";
    }
}