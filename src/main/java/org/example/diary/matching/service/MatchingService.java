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
        List<User> potentialMatches = matchRepository.findByMbtiNot(user.getMbti());

        User matchedUser = null;
        if (!potentialMatches.isEmpty()) {
            Random random = new Random();
            matchedUser = potentialMatches.get(random.nextInt(potentialMatches.size()));
        } else {
            potentialMatches = matchRepository.findByMbti(user.getMbti());
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

        MatchingHistory matchingHistory = new MatchingHistory();
        matchingHistory.setUser1(user);
        matchingHistory.setUser2(matchedUser);
        matchingHistory.setStartDate(new Date());
        matchingHistoryRepository.save(matchingHistory);

        return matchedUser;
    }

    @Transactional
    public void cancelMatch(Long matchingHistoryId) {
        // 매칭 이력을 조회
        MatchingHistory matchingHistory = matchingHistoryRepository.findById(matchingHistoryId)
                .orElseThrow(() -> new RuntimeException("Matching history not found"));

        User user1 = matchingHistory.getUser1();
        User user2 = matchingHistory.getUser2();

        // 매칭 상태 업데이트
        user1.setIsMatched(0);
        user2.setIsMatched(0);
        userRepository.save(user1);
        userRepository.save(user2);

        matchingHistory.setEndDate(new Date());
        matchingHistoryRepository.save(matchingHistory);
    }

    public Optional<MatchingHistory> findExistingMatch(User user) {
        return matchingHistoryRepository.findByUser1OrUser2AndEndDateIsNull(user, user);
    }
}
