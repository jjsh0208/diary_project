package org.example.diary.diary;

import lombok.RequiredArgsConstructor;

import org.example.diary.Security.SecurityUtil;
import org.example.diary.exception.DataNotFoundException;
import org.example.diary.user.User;
import org.example.diary.user.UserService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    private final UserService userService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void diaryCreate( String title, String content , MultipartFile imgFile,
                            String music_url) throws IOException{

        String filePath = null;

        if (imgFile != null && !imgFile.isEmpty()) {
            // 업로드 디렉토리가 존재하지 않으면 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            filePath = uploadDirFile.getAbsolutePath() + File.separator + imgFile.getOriginalFilename();

            imgFile.transferTo(new File(filePath));

            String os = System.getProperty("os.name").toLowerCase();
            String separator = "";

            if (os.contains("win")) {
                separator = "\\uploads\\";
            } else if (os.contains("mac")) {
                separator = "/uploads/";
            }

            if (!separator.isEmpty()) {
                filePath = filePath.substring(filePath.lastIndexOf(separator));
            }
        }

        create(title,content,filePath,music_url);
    }

    private void create(String subject, String content ,String imgPath,String music_url){
        User writer = userService.getUser(SecurityUtil.getCurrentUsername());

        Diary diary = Diary.builder()
                .writer(writer)
                .subject(subject)
                .content(content)
                .imgFile(imgPath)
                .music_url(music_url)
                .date(new Date())
                .build();

        diaryRepository.save(diary);
    }

    public Diary getDiary(Long id) {
        Optional<Diary> diary = this.diaryRepository.findById(id);
        if (diary.isPresent()){
            return diary.get();
        }else{
            throw new DataNotFoundException("Diary Not Fount");
        }
    }


    public Diary getPartnerDiary(Long id,Date date) {
         Optional<Diary> diary = diaryRepository.findByIdAndDate(id,date);
         if (diary.isPresent()){
             return diary.get();
         }else{
             throw  new DataNotFoundException("partnerDiary Not Fount");
         }
    }


    public List<Diary> getList() {
        return diaryRepository.findAll();
    }

}
