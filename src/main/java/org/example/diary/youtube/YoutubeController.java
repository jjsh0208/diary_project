package org.example.diary.youtube;

import org.example.diary.youtube.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("/youtube")
public class YoutubeController {
    private final YoutubeService youtubeService;
    @GetMapping
    public String searchVideo(@RequestParam String keyword, Model model) throws IOException {
        HashMap<String, String> result = youtubeService.searchVideo(keyword);

        if (result == null)
            return "redirect:diary/diaryForm";
        model.addAttribute("youtubeInfo", result);
        return "youtube";
    }
}
