package org.example.diary.diary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    Optional<Diary> findByIdAndDate(Long id, Date date);
}
