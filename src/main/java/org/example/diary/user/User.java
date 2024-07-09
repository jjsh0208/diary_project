package org.example.diary.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.diary.diary.Diary;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false, unique = true)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 13, nullable = false)
    private String phone;

    @Column(length = 20, nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column
    private Date birthDate;

    @Column(length = 1, nullable = false)
    private String sex;

    @Column(length = 4, nullable = false)
    private String mbti;

    // 매칭 여부 추가
    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int isMatched = 0;

    @OneToMany(mappedBy = "writer") // Diary 엔티티의 user 필드에 매핑됨
    private List<Diary> diaries;
}
