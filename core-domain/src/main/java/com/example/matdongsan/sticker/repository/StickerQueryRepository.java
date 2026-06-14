package com.example.matdongsan.sticker.repository;

import com.example.matdongsan.sticker.domain.Sticker;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StickerQueryRepository {

    List<Sticker> findAllActiveOrderByDisplayOrder();

    Optional<Sticker> findById(Long id);

    List<Sticker> findAllByIdIn(Collection<Long> ids);
}
