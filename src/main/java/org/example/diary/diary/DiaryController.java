package org.example.diary.diary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/write")
    public String diaryCreate(DiaryForm diaryForm){
        return "diary/diaryForm";
    }

    @PostMapping("/write")
    public String diaryCreate(@Valid DiaryForm diaryForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "diary/diaryForm";
        }

        diaryService.create(diaryForm.getTitle(), diaryForm.getContent(), diaryForm.getDate());
        return "redirect:/diary/write";
    }
}