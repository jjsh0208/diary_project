package org.example.diary.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private String name;
    private String phone;
    private String sex;
    private String mbti;
}
