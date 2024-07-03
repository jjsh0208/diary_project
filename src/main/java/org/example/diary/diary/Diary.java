package org.example.diary.diary;

import jakarta.persistence.*;
import lombok.*;
import org.example.diary.matching.entity.MatchingHistory;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long writer;

    @Column(length = 50 , nullable = false)
    private String title;

    @Column(length = 500 , nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column
    private String imgFile;

    @Column
    private String music_url;

    @Column
    private Date date;

    @ManyToOne
    @JoinColumn(name = "matching_history_id")
    private MatchingHistory matchingHistory;

}
