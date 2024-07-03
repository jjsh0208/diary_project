package org.example.diary.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    
    //회원 id 와 현재 달의 게시글만 받아옴
    List<Diary> findByWriter_IdAndDateBetween(Long writerId, LocalDate startDate, LocalDate endDate);
}
