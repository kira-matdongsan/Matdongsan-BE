package com.example.matdongsan.sticker.mapper;

import com.example.matdongsan.jpa.entity.sticker.StickerEntity;
import com.example.matdongsan.sticker.domain.Sticker;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StickerMapper {

    Sticker toDomain(StickerEntity entity);

    List<Sticker> toDomainList(List<StickerEntity> entities);
}
