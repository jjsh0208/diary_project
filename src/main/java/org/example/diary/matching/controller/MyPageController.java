package org.example.diary.matching.controller;

import lombok.RequiredArgsConstructor;
import org.example.diary.diary.Diary;
import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.matching.service.HistoryService;
import org.example.diary.matching.service.MatchingService;
import org.example.diary.user.User;
import org.example.diary.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserRepository userRepository;
    private final MatchingService matchingService;
    private final HistoryService historyService;

    @GetMapping("/{id}")
    public String getUserPage(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);

            Optional<MatchingHistory> matchingHistoryOptional = matchingService.findExistingMatch(user);
            boolean isMatched = matchingHistoryOptional.isPresent();
            model.addAttribute("isMatched", isMatched);
            if (isMatched) {
                model.addAttribute("matchingHistoryId", matchingHistoryOptional.get().getId());
                model.addAttribute("matchedUser", matchingHistoryOptional.get()
                        .getUser1().equals(user) ? matchingHistoryOptional.get().getUser2() : matchingHistoryOptional.get().getUser1());
            }
        }
        return "mypage/mypage";
    }

    // 히스토리 보기
    @GetMapping("/{id}/myHistory")
    public String getUserHistory(@PathVariable Long id, Model model) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            List<MatchingHistory> historyList = historyService.getUserHistory(id);
            model.addAttribute("historyList", historyList);
        }
        return "mypage/history";
    }

    // 히스토리 상세 (matching_history의 id값을 가져옴)
    @GetMapping("/historyShow/{matching_history_id}")
    public String showHistoryDetails(@PathVariable Long matching_history_id, Model model) {
        // matching_history_id로 MatchingHistory 엔티티 조회
        MatchingHistory matchingHistory = historyService.findById(matching_history_id)
                .orElseThrow(() -> new RuntimeException("Matching history not found"));

        // matching_history_id로 해당하는 다이어리 목록 조회
        List<Diary> diaries = historyService.findDiariesByMatchingHistoryId(matching_history_id);

        // 조회된 히스토리와 다이어리 목록을 모델에 추가
        model.addAttribute("matchingHistory", matchingHistory);
        model.addAttribute("diaries", diaries);

        // "diary/diaryShow" 뷰를 반환
        return "diary/diaryShow";
    }


}
