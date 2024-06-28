package org.example.diary;

import org.example.diary.diary.Diary;
import org.example.diary.diary.DiaryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class DiaryApplicationTests {
	@Autowired
	private DiaryRepository diaryRepository;

	@Test
	void testJpa(){
		Diary diary = Diary.builder()
				.title("test")
				.content("test")
				.build();

		diaryRepository.save(diary);
	}


}
