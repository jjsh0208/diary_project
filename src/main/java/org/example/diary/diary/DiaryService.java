package org.example.diary.diary;

import jakarta.servlet.http.HttpSession;
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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
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

            // 파일 이름에 타임스탬프 추가하여 고유하게 만듦
            String originalFileName = imgFile.getOriginalFilename();
            String newFileName = generateUniqueFileName(originalFileName);


            filePath = uploadDirFile.getAbsolutePath() + File.separator + newFileName;

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

    private String generateUniqueFileName(String originalFileName) {
        String extension = "";
        String fileNameWithoutExtension = originalFileName;

        //확장자 분리
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex != -1) {
            //확장자 추출
            extension = originalFileName.substring(dotIndex); 
            //확장자를 제외한 파일명 추출
            fileNameWithoutExtension = originalFileName.substring(0, dotIndex); 
        }

        // 타임스탬프 추가
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

        // 파일명에 타임스탬프 추가하여 고유한 파일명 생성
        return fileNameWithoutExtension  + "_t" + timeStamp + extension;
    }


    private void create(String subject, String content ,String imgPath,String music_url){
        User writer = userService.getUser(SecurityUtil.getCurrentUsername());
        LocalDate date = LocalDate.now();

        Diary diary = Diary.builder()
                .writer(writer)
                .subject(subject)
                .content(content)
                .imgFile(imgPath)
                .music_url(music_url)
                .date(date)
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

    public List<Diary> getMonthlyDiary(Long id) {
        YearMonth currentMonth = YearMonth.now();
        LocalDate startDate = currentMonth.atDay(1);
        LocalDate endDate = currentMonth.atEndOfMonth();

        return diaryRepository.findByWriter_IdAndDateBetween(id,startDate,endDate);
    }




}
