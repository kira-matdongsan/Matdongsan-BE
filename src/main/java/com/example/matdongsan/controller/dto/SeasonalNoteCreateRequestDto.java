package com.example.matdongsan.controller.dto;

import com.example.matdongsan.service.dto.SeasonalNoteServiceDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "제철기록 작성 요청 DTO")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SeasonalNoteCreateRequestDto {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate recordedDate;

    private List<String> imageUrls;

    public SeasonalNoteServiceDto toServiceDto() {
        return SeasonalNoteServiceDto.builder()
                .content(content)
                .recordedDate(recordedDate)
                .imageUrls(imageUrls)
                .build();
    }
}
