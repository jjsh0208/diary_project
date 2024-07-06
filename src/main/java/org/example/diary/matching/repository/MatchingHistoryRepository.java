package org.example.diary.matching.repository;

import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MatchingHistoryRepository extends JpaRepository<MatchingHistory, Long> {

    List<MatchingHistory> findByUser1IdOrUser2Id(Long userId, Long userId1);

    List<MatchingHistory> findByUser1OrUser2AndEndDateIsNull(User user1, User user2);

}

