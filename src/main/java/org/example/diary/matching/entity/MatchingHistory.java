package org.example.diary.matching.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.diary.user.User;

import java.time.LocalDate;

@Data
@Entity
public class MatchingHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private User user2;

    private LocalDate startDate;
    private LocalDate endDate;
}
