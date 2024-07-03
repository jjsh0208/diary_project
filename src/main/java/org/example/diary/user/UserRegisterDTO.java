package org.example.diary.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRegisterDTO {
    private String email;

    private String password;

    private String phone;

    private String name;

    private String birthDate; //문자열로 받아와서 Date로 생성에 DB에 생성

    private String sex;

    private String mbti;
}