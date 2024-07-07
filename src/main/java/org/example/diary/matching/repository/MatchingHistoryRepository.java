package org.example.diary.matching.repository;

import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MatchingHistoryRepository extends JpaRepository<MatchingHistory, Long> {

    Optional<MatchingHistory> findByUser1_Id(Long userId);
    Optional<MatchingHistory> findByUser2_Id(Long userId);

//    List<MatchingHistory> findByUser1IdOrUser2Id(Long userId, Long userId1);

    List<MatchingHistory> findByUser1OrUser2AndEndDateIsNull(User user1, User user2);

    @Query("SELECT CASE "
            + " WHEN mh.user1.id = :userId THEN mh.user2.id "
            + " WHEN mh.user2.id = :userId THEN mh.user1.id "
            + " ELSE NULL END "
            + " FROM MatchingHistory mh "
            + " WHERE mh.user1.id = :userId OR mh.user2.id = :userId")
    Long findOppositeUserIdById(@Param("userId") Long userId);

    @Query("SELECT mh "
            + " FROM MatchingHistory mh "
            + " WHERE mh.user1.id = :userId OR mh.user2.id = :userId")
    Optional<MatchingHistory> findMatchingHistoryByUserId(@Param("userId") Long userId);


    List<MatchingHistory> findByUser1Id(Long userId);
    List<MatchingHistory> findByUser2Id(Long userId);
    List<MatchingHistory> findByUser1IdOrUser2Id(Long userId1, Long userId2); // Assuming you have user1Id and user2Id fields


}

