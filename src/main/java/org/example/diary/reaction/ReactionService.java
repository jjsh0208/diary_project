package org.example.diary.reaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;

    public Reaction addReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    public List<Reaction> getReactionsByDiaryId(Long diaryId) {
        return reactionRepository.findByDiaryId(diaryId);
    }

    public List<Reaction> getReactionsByUserId(Long userId) {
        return reactionRepository.findByUserId(userId);
    }

    public void deleteReaction(Long id) {
        reactionRepository.deleteById(id);
    }
}
