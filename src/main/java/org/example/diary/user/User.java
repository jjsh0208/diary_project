package org.example.diary.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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

    @Column(length = 15, nullable = false)
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
}
