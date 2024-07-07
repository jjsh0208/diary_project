package org.example.diary.reaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reaction")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @PostMapping
    public Reaction addReaction(@RequestBody Reaction reaction) {
        return reactionService.addReaction(reaction);
    }

    @GetMapping("/diary/{diaryId}")
    public List<Reaction> getReactionsByDiaryId(@PathVariable Long diaryId) {
        return reactionService.getReactionsByDiaryId(diaryId);
    }

    @GetMapping("/user/{userId}")
    public List<Reaction> getReactionsByUserId(@PathVariable Long userId) {
        return reactionService.getReactionsByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public void deleteReaction(@PathVariable Long id) {
        reactionService.deleteReaction(id);
    }
}
