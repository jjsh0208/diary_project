package org.example.diary.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class UserRegisterDTO {
    private String email;

    private String password;

    private String phone;

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;

    private String sex;

    private String mbti;
}