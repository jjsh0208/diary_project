package org.example.diary.diary;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class DiaryService {
    private final DiaryRepository diaryRepository;

    public void create(String title, String content , Date date){
        Diary diary = Diary.builder()
                .title(title)
                .content(content)
                .date(date)
                .build();
        diaryRepository.save(diary);
    }
}
