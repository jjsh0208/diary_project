package org.example.diary.matching.service;

import lombok.RequiredArgsConstructor;
import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.matching.repository.MatchRepository;
import org.example.diary.matching.repository.MatchingHistoryRepository;
import org.example.diary.user.User;
import org.example.diary.user.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class MatchingService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchingHistoryRepository matchingHistoryRepository;

    @Transactional
    public Optional<User> findMatch(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 중복 매칭 신청 불가
        if (user.getIsMatched() == 1) {
            throw new RuntimeException("User is already matched");
        }

        // 1. 나를 제외한 유저 목록에서 mbti가 다른 사람 찾아서 매칭
        List<User> potentialMatches = matchRepository.findByMbtiNotAndIsMatched(user.getMbti(), 0)
                .stream()
                .filter(potentialMatch -> !potentialMatch.getId().equals(userId))
                .collect(Collectors.toList());

        User matchedUser = null;
        if (!potentialMatches.isEmpty()) {
            Random random = new Random();
            matchedUser = potentialMatches.get(random.nextInt(potentialMatches.size()));
        } else {
            // 2. 나를 제외한 유저 목록에서 mbti가 다른 사람이 없다면, mbti가 같은 사람 중에서 찾기
            potentialMatches = matchRepository.findByMbtiAndIsMatched(user.getMbti(), 0)
                    .stream()
                    .filter(potentialMatch -> !potentialMatch.getId().equals(userId))
                    .collect(Collectors.toList());
            if (!potentialMatches.isEmpty()) {
                matchedUser = potentialMatches.get(new Random().nextInt(potentialMatches.size()));
            }
        }

        if (matchedUser == null) {
            return Optional.empty(); // 예외 대신 Optional.empty() 반환
        }

        // 매칭 상태 업데이트
        user.setIsMatched(1);
        matchedUser.setIsMatched(1);
        userRepository.save(user);
        userRepository.save(matchedUser);

        // 히스토리 업데이트
        MatchingHistory matchingHistory = new MatchingHistory();
        matchingHistory.setUser1(user);
        matchingHistory.setUser2(matchedUser);
        matchingHistory.setStartDate(LocalDate.now());
        matchingHistoryRepository.save(matchingHistory);

        return Optional.of(matchedUser);
    }

    @Transactional
    public void cancelMatch(Long matchingHistoryId) {
        // 매칭 이력 조회
        MatchingHistory matchingHistory = matchingHistoryRepository.findById(matchingHistoryId)
                .orElseThrow(() -> new RuntimeException("Matching history not found"));

        // 매칭 이력 역순 조회
        List<MatchingHistory> matchingHistories = matchingHistoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        // 매칭 이력이 존재하는지 확인
        if (matchingHistories.isEmpty()) {
            throw new RuntimeException("Matching history not found");
        }

        // 가장 최신의 매칭 이력 가져오기
        matchingHistory = matchingHistories.get(0);

        // 이미 종료된 매칭이력인지 확인
        if (matchingHistory.getEndDate() != null) {
            throw new RuntimeException("Matching history is already canceled");
        }

        // 명확한 사용자 구분을 위해 user1과 user2 가져오기
        User user1 = matchingHistory.getUser1();
        User user2 = matchingHistory.getUser2();

        // 현재 매칭 상태 확인
        if (user1.getIsMatched() == 0 || user2.getIsMatched() == 0) {
            throw new RuntimeException("Users are not currently matched");
        }

        // 매칭 상태 업데이트
        user1.setIsMatched(0);
        user2.setIsMatched(0);
        userRepository.save(user1);
        userRepository.save(user2);

        // 매칭 종료 날짜 설정 및 저장
        matchingHistory.setEndDate(LocalDate.now());
        matchingHistoryRepository.save(matchingHistory);
    }

    // 종료되지 않은 매칭 찾기
    public Optional<MatchingHistory> findExistingMatch(User user) {
        List<MatchingHistory> matches = matchingHistoryRepository.findByUser1OrUser2AndEndDateIsNull(user, user);
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
    }
}
