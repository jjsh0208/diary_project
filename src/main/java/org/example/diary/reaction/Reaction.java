package org.example.diary.reaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.diary.user.User;
import org.example.diary.diary.Diary;

@Getter
@Setter
@Entity
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    @Column(length = 10, nullable = false)
    private String emoji;
}
