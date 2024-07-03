package org.example.diary.matching.service;

import lombok.RequiredArgsConstructor;
import org.example.diary.diary.Diary;
import org.example.diary.diary.DiaryRepository;
import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.matching.repository.MatchingHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final MatchingHistoryRepository matchingHistoryRepository;
    private final DiaryRepository diaryRepository;

    public List<MatchingHistory> getUserHistory(Long userId) {
        return matchingHistoryRepository.findByUser1IdOrUser2Id(userId, userId);
    }

    public Optional<MatchingHistory> findById(Long id) {
        return matchingHistoryRepository.findById(id);
    }

    public List<Diary> findDiariesByMatchingHistoryId(Long matchingHistoryId) {
        return diaryRepository.findByMatchingHistoryId(matchingHistoryId);
    }

}
