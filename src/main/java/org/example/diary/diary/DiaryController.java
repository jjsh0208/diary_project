package org.example.diary.diary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @GetMapping("/list")
    public String diaryList(Model model){
        List<Diary> diaries = diaryService.getList();

        model.addAttribute("diaryList" , diaries);
        return "index";
    }

    @GetMapping("/write")
    public String diaryCreate(DiaryFormDTO diaryForm){
        return "diary/diaryForm";
    }

    @PostMapping("/write")
    public String diaryCreate(@Valid DiaryFormDTO diaryForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "diary/diaryForm";
        }

        // 파일 업로드 처리
        MultipartFile file = diaryForm.getImgFile();

        try{
            diaryService.diaryCreate(diaryForm.getWriter(),diaryForm.getTitle(),diaryForm.getContent(),
                    file,diaryForm.getMusic_url(),diaryForm.getDate());
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