package org.example.diary.reaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findByDiaryId(Long diaryId);
    List<Reaction> findByUserId(Long userId);
}