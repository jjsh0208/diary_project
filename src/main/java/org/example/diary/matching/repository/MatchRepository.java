package org.example.diary.matching.repository;

import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<User, Long> {

    // 다른 MBTI를 가진 유저를 찾기
    @Query("SELECT u FROM User u WHERE u.mbti <> ?1")
    List<User> findByMbtiNot(String mbti);

    // 같은 MBTI를 가진 유저를 찾기
    List<User> findByMbti(String mbti);
}
