package org.example.diary.diary;

import org.example.diary.matching.entity.MatchingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    Optional<Diary> findByIdAndDate(Long id, Date date);
    
    // 히스토리 조회를 위한 diary의 matching_history_id 조회
    List<Diary> findByMatchingHistoryId(Long matchingHistoryId);

}
