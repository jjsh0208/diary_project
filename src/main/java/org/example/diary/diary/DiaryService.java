package org.example.diary.diary;

import lombok.RequiredArgsConstructor;
import org.example.diary.excetion.DataNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public void diaryCreate(Long writer, String title, String content , MultipartFile imgFile,
                            String music_url, Date date) throws IOException{

        String filePath = null;

        if (imgFile != null && !imgFile.isEmpty()) {
            // 업로드 디렉토리가 존재하지 않으면 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }

            filePath = uploadDirFile.getAbsolutePath() + File.separator + imgFile.getOriginalFilename();

            imgFile.transferTo(new File(filePath));

            filePath = filePath.substring(filePath.lastIndexOf("\\uploads\\"));
        }

        create(writer,title,content,filePath,music_url,date);
    }

    private void create(Long writer,String title, String content ,String imgPath,String music_url, Date date){

        Diary diary = Diary.builder()
                .writer(writer)
                .title(title)
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
