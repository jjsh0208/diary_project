package org.example.diary.diary;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Data
public class DiaryForm {

    @NotEmpty(message = "제목을 작성해주세요!")
    private String title;

    @NotEmpty(message = "일기를 작성해주세요!")
    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

}
