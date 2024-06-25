package org.example.diary.diary;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @GetMapping("/write")
    public String diaryCreate(DiaryForm diaryForm){
        return "diary/diaryForm";
    }

    @PostMapping("/write")
    public String diaryCreate(@Valid DiaryForm diaryForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "diary/diaryForm";
        }

        // 파일 업로드 처리
        MultipartFile file = diaryForm.getImg_path();


        if (file != null && !file.isEmpty()) {
            try {
                // 업로드 디렉토리가 존재하지 않으면 생성
                File uploadDirFile = new File(uploadDir);
                if (!uploadDirFile.exists()) {
                    uploadDirFile.mkdirs();
                }

                String filePath = uploadDirFile.getAbsolutePath() + File.separator + file.getOriginalFilename();
                file.transferTo(new File(filePath));
                diaryService.create(diaryForm.getWriter(), diaryForm.getTitle(),
                        diaryForm.getContent(),filePath , diaryForm.getMusic_url(), diaryForm.getDate());

                return "redirect:/diary/write";
            } catch (IOException e) {
                bindingResult.rejectValue("file", "fileUploadError", "파일 업로드에 실패했습니다.");
                e.printStackTrace(); // 에러 로그 출력
                return "diary/diaryForm";
            }
        }

        diaryService.create(diaryForm.getWriter(), diaryForm.getTitle(),
                diaryForm.getContent(), null,diaryForm.getMusic_url(), diaryForm.getDate());



        return "redirect:/diary/write";
    }
}