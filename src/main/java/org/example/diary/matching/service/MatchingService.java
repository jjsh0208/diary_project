package org.example.diary.matching.service;

import lombok.RequiredArgsConstructor;
import org.example.diary.matching.entity.MatchingHistory;
import org.example.diary.matching.repository.MatchRepository;
import org.example.diary.matching.repository.MatchingHistoryRepository;
import org.example.diary.user.User;
import org.example.diary.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final UserRepository userRepository;
    private final MatchRepository matchRepository;
    private final MatchingHistoryRepository matchingHistoryRepository;

    @Transactional
    public User findMatch(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 중복 매칭 신청 불가
        if (user.getIsMatched() == 1) {
            throw new RuntimeException("User is already matched");
        }

        // is_matched=0인 유저 중에서 매칭 시도
        // 매칭 후보 리스트
        List<User> potentialMatches = matchRepository.findByMbtiNotAndIsMatched(user.getMbti(), 0);

        User matchedUser = null;
        if (!potentialMatches.isEmpty()) {
            Random random = new Random();
            matchedUser = potentialMatches.get(random.nextInt(potentialMatches.size()));
        } else {
            potentialMatches = matchRepository.findByMbtiAndIsMatched(user.getMbti(), 0);
            if (!potentialMatches.isEmpty()) {
                matchedUser = potentialMatches.get(new Random().nextInt(potentialMatches.size()));
            }
        }

        if (matchedUser == null) {
            throw new RuntimeException("No available match found");
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
        matchingHistory.setStartDate(new Date());
        matchingHistoryRepository.save(matchingHistory);

        return matchedUser;
    }

    @Transactional
    public void cancelMatch(Long matchingHistoryId) {
        // 매칭 이력 조회
        MatchingHistory matchingHistory = matchingHistoryRepository.findById(matchingHistoryId)
                .orElseThrow(() -> new RuntimeException("Matching history not found"));

        // 이미 종료된 매칭이력인지 확인
        if (matchingHistory.getEndDate() != null) {
            throw new RuntimeException("Matching history is already canceled");
        }

        // 명확한 사용자 구분을 위해 user1과 user2 가져오기
        User user1 = matchingHistory.getUser1();
        User user2 = matchingHistory.getUser2();

        // 매칭 상태 업데이트
        user1.setIsMatched(0);
        user2.setIsMatched(0);
        userRepository.save(user1);
        userRepository.save(user2);

        // 매칭 종료 날짜 설정 및 저장
        matchingHistory.setEndDate(new Date());
        matchingHistoryRepository.save(matchingHistory);
    }

    // 종료되지 않은 매칭 찾기
    public Optional<MatchingHistory> findExistingMatch(User user) {
        List<MatchingHistory> matches = matchingHistoryRepository.findByUser1OrUser2AndEndDateIsNull(user, user);
        return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0));
    }
}
