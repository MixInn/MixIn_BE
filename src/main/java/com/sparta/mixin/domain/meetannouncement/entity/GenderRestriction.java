package com.sparta.mixin.domain.meetannouncement.entity;

public enum GenderRestriction {
    MALE_ONLY("남자만"),
    FEMALE_ONLY("여자만"),
    NO_RESTRICTION("상관없음");

    private final String description;

    GenderRestriction(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // String 값을 enum으로 변환해주는 메서드
    public static GenderRestriction fromString(String description) {
        for (GenderRestriction genderRestriction : GenderRestriction.values()) {
            if (genderRestriction.getDescription().equalsIgnoreCase(description)) {
                return genderRestriction;
            }
        }
        throw new IllegalArgumentException("Unknown gender restriction: " + description);
    }
}
