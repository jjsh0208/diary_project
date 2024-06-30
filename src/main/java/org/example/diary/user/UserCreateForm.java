package org.example.diary.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class UserCreateForm {
    @Size(min = 5, max = 30)
    @NotEmpty(message = "이메일을 입력해 주세요.")
    private String email;

    @Size(min = 5, max = 30)
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String password;

    @Size(min = 5, max = 30)
    @NotEmpty(message = "비밀번호 확인을 해주세요.")
    private String confirmPassword;

    @Size(max = 13)
    @NotEmpty(message = "전화번호를 입력해 주세요.")
    private String phone;

    @Size
    @NotEmpty(message = "이름을 입력해 주세요.")
    private String name;

    private Date birthDate;

    @NotEmpty(message = "성별을 입력해 주세요.")
    private String sex;

    @NotEmpty(message = "MBTI를 입력해 주세요.")
    private String mbti;
}
