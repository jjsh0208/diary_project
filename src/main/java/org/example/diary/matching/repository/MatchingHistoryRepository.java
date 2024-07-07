package org.example.diary.matching.repository;

import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MatchingHistoryRepository extends JpaRepository<MatchingHistory, Long> {

    List<MatchingHistory> findByUser1IdOrUser2Id(Long userId, Long userId1);

    List<MatchingHistory> findByUser1OrUser2AndEndDateIsNull(User user1, User user2);

    @Query("SELECT CASE "
            + " WHEN mh.user1.id = :userId THEN mh.user2.id "
            + " WHEN mh.user2.id = :userId THEN mh.user1.id "
            + " ELSE null END "
            + " FROM MatchingHistory mh "
            + " WHERE mh.user1.id = :userId OR mh.user2.id = :userId")
    Long findOppositeUserIdById(@Param("userId") Long userId);

    @Query("SELECT mh "
            + " FROM MatchingHistory mh "
            + " WHERE mh.user1.id = :userId OR mh.user2.id = :userId")
    Optional<MatchingHistory> findMatchingHistoryByUserId(@Param("userId") Long userId);


}

