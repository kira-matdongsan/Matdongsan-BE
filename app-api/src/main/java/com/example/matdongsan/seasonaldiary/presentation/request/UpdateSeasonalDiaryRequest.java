package com.example.matdongsan.seasonaldiary.presentation.request;

import com.example.matdongsan.seasonaldiary.application.dto.UpdateSeasonalDiaryParam;
import com.example.matdongsan.seasonaldiary.enums.SeasonalDiarySticker;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateSeasonalDiaryRequest {

    @NotNull
    private SeasonalDiarySticker sticker;

    @NotBlank
    @Size(max = 40)
    private String content;

    public UpdateSeasonalDiaryParam toParam() {
        return UpdateSeasonalDiaryParam.builder()
                .sticker(sticker)
                .content(content)
                .build();
    }
}
