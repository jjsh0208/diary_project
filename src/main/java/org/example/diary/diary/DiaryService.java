package org.example.diary.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public void create(Long writer,String title, String content ,String imgPath,String music_url, Date date){
        Diary diary = Diary.builder()
                .title(title)
                .content(content)
                .img_path(imgPath)
                .music_url(music_url)
                .date(date)
                .build();
        diaryRepository.save(diary);
    }
}
