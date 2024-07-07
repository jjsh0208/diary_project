package org.example.diary.matching.repository;

import org.example.diary.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchRepository extends JpaRepository<User, Long> {

    List<User> findByMbtiNotAndIsMatched(String mbti, int isMatched);
    List<User> findByMbtiAndIsMatched(String mbti, int isMatched);

}
