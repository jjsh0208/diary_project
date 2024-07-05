package org.example.diary.diary;

import jakarta.persistence.*;
import lombok.*;

import org.example.diary.user.User;


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

    @ManyToOne
    @JoinColumn(name = "user_id") // User 엔티티의 기본 키 컬럼명인 id로 수정
    private User writer;

    @Column(length = 50 , nullable = false)
    private String subject;

    @Column(length = 500 , nullable = false, columnDefinition = "TEXT")

    private String content;

    @Column
    private String imgFile;

    @Column
    private String music_url;

    @Column
    private Date date;

}
