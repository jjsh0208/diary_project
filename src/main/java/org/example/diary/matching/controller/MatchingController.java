package org.example.diary.matching.controller;

import lombok.RequiredArgsConstructor;
import org.example.diary.matching.service.MatchingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/find-match")
    public String findMatch(@RequestParam Long userId) {
        matchingService.findMatch(userId);
        return "redirect:/mypage/" + userId;
    }

    // 매칭 취소 버튼 일단 여기로 돌려둠
    @GetMapping("/cancel-match")
    public String cancelMatch(@RequestParam Long matchingHistoryId, @RequestParam Long userId) {
        matchingService.cancelMatch(matchingHistoryId);
        return "redirect:/mypage/index";
    }
}
