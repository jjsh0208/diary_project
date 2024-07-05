package org.example.diary.diary;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@Data
public class DiaryFormDTO {

    private Long writer;

    @NotEmpty(message = "제목을 작성해주세요!")
    private String subject;

    @NotEmpty(message = "일기를 작성해주세요!")
    private String content;

    private MultipartFile imgFile;

    private String Music_url;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
}
