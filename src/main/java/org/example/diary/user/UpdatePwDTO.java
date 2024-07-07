package org.example.diary.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePwDTO {
    private String password;
    private String confirmPw;
}
