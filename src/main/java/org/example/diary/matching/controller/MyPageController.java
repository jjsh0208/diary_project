package org.example.diary.matching.controller;

import lombok.RequiredArgsConstructor;
import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.matching.service.MatchingService;
import org.example.diary.user.User;
import org.example.diary.user.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final UserRepository userRepository;
    private final MatchingService matchingService;

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
                model.addAttribute("matchedUser", matchingHistoryOptional.get().getUser1().equals(user) ? matchingHistoryOptional.get().getUser2() : matchingHistoryOptional.get().getUser1());
            }
        } else {
            return "error/404";
        }
        return "mypage/mypage";
    }
}
