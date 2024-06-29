package org.example.diary.matching.repository;

import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatchingHistoryRepository extends JpaRepository<MatchingHistory, Long> {
    MatchingHistory findByUser1AndUser2AndEndDateIsNull(User user1, User user2); // 매칭 히스토리 검색
    Optional<MatchingHistory> findByUser1OrUser2AndEndDateIsNull(User user, User user1);
}
