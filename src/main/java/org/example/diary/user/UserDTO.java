package org.example.diary.user;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserDTO {
    private String email;

    private String password;

    private String phone;

    private String name;


    private Date birthDate;

    private String sex;

    private String mbti;
}
