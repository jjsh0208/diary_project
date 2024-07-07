package org.example.diary.diary;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
import java.time.LocalDate;
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

        LocalDate today = LocalDate.now(); //오늘 날짜 게시글 작성 유무 체크용

        boolean hasTodayDiary = diaries.stream() //가져온 게시글 중에 오늘날짜와 동일한 것이 있으면 true 없으면 false
                .anyMatch(diary -> diary.getDate().equals(today));


        System.out.println("너는 무엇이냐 : " + hasTodayDiary);
        model.addAttribute("diaryList" , diaries);
        model.addAttribute("user",user);
        model.addAttribute("hasTodayDiary",hasTodayDiary);

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
        Diary partnerDiary = diaryService.getPartnerDiary(diary.getWriter().getId() ,diary.getDate());
        //다이어리 작성일이 같고 매칭 되어 있는 상대의 글
        //매칭되어있는 상대 확인 법

        //작성자 와  매칭되어있고 and 같은 날짜의 글을 가져온다.
        model.addAttribute("diary",diary);
        model.addAttribute("partner",partnerDiary);

        return  "diary/diaryShow";
    }
}