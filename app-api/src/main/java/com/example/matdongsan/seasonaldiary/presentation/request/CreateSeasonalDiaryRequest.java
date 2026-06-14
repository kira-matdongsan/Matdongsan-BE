package com.example.matdongsan.seasonaldiary.presentation.request;

import com.example.matdongsan.seasonaldiary.application.dto.CreateSeasonalDiaryParam;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class CreateSeasonalDiaryRequest {

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recordDate;

    @NotNull
    private SeasonalDiarySticker sticker;

    @NotBlank
    @Size(max = 40)
    private String content;

    public CreateSeasonalDiaryParam toParam() {
        return CreateSeasonalDiaryParam.builder()
                .recordDate(recordDate)
                .sticker(sticker)
                .content(content)
                .build();
    }
}
