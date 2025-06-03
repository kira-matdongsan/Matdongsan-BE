package com.example.matdongsan.common.util.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    FOOD_STORY("food-story", Visibility.PUBLIC),
    PROFILE_IMAGE("profile-image", Visibility.PRIVATE),
    DISH("dish", Visibility.PUBLIC);

    private final String dirName;
    private final Visibility visibility;

    @Getter
    @RequiredArgsConstructor
    public enum Visibility {
        PUBLIC("public"),
        PRIVATE("private");

        private final String type;
    }
}
