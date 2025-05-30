package com.example.matdongsan.service.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlaceServiceDto {

    private final String name;
    private final String category;
    private final String address;
    private final String naverUrl;

}
