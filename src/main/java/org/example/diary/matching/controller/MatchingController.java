package org.example.diary.matching.controller;

import lombok.RequiredArgsConstructor;
import org.example.diary.matching.service.MatchingService;
import org.example.diary.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    // 매칭 신청
//    @GetMapping("/find-match")
//    public String findMatch(@RequestParam Long userId) {
//            matchingService.findMatch(userId);
//            return "redirect:/";
//    }

    // 매칭 신청
    @GetMapping("/find-match")
    public String findMatch(@RequestParam Long userId, RedirectAttributes redirectAttributes) {
        Optional<User> matchedUser = matchingService.findMatch(userId);
        if (matchedUser.isPresent()) {
            redirectAttributes.addFlashAttribute("matchSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("matchSuccess", false);
        }
        return "redirect:/";
    }

    // 매칭 취소 버튼 일단 여기로 돌려둠
    @GetMapping("/cancel-match")
    public String cancelMatch(@RequestParam Long matchingHistoryId) {
        matchingService.cancelMatch(matchingHistoryId);
        return "redirect:/";
    }
}
