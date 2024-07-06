package org.example.diary.diary;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.diary.Security.SecurityUtil;

import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.matching.service.MatchingService;
import org.example.diary.user.User;
import org.example.diary.user.UserRepository;
import org.example.diary.user.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    private final UserService userService;

    private final UserRepository userRepository;

    private final MatchingService matchingService;


    @GetMapping("/list")
    public String diaryList(Model model, HttpSession session){

        // 세션에서 사용자 정보를 가져와 모델에 추가
        User user = userService.UserSessionCheck(session);

        List<Diary> diaries = diaryService.getMonthlyDiary(user.getId());


        // 추가된 변경 대상 검사 및 세션 업데이트
        Optional<User> optionalUser = userRepository.findById(user.getId()); // db에 저장되어 있는 동일한 객체 (매치드 변경이 반영되어있다.)
        if (optionalUser.isPresent()){ // 가져온 user가 존재하면
            User dbUser = optionalUser.get();
            if (dbUser.getIsMatched() != user.getIsMatched()){ // 서로의 매치드 값이 다르면
                session.setAttribute("currentUser", dbUser);
                user = dbUser; // 모델에 추가할 user도 업데이트
            }
        }

        // MatchingHistoryId 조회 및 모델에 추가
        Optional<MatchingHistory> matchingHistoryOptional = matchingService.findExistingMatch(user);
        if (matchingHistoryOptional.isPresent()) {
            Long matchingHistoryId = matchingHistoryOptional.get().getId();
            model.addAttribute("matchingHistoryId", matchingHistoryId);
        } else {
            model.addAttribute("matchingHistoryId", null);
        }

        model.addAttribute("diaryList" , diaries);
        model.addAttribute("user",user);

        return "diary/index";
    }

    @GetMapping("/write")
    public String diaryCreate(DiaryFormDTO diaryForm ,Model model ,HttpSession session){

        // 세션에서 사용자 정보를 가져와 모델에 추가
        User user = userService.UserSessionCheck(session);

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
        Diary diary = diaryService.getDiary(id);

        model.addAttribute(diary);

        return  "diary/diaryShow";
    }


}