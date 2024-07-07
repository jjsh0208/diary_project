package org.example.diary.diary;

import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    //회원 id 와 현재 달의 게시글만 받아옴
    List<Diary> findByWriter_IdAndDateBetween(Long writerId, LocalDate startDate, LocalDate endDate);

    // 히스토리 조회를 위한 diary의 matching_history_id 조회
    List<Diary> findByMatchingHistoryId(Long matchingHistoryId);

    // 작성자의 해당 날짜 게시글을 찾음
    Diary findByWriterAndDate(User writer, LocalDate date);


}
