package com.sparta.mixin.domain.profile.entity;

public enum Interest {

    IT_DEVELOPMENT(Description.IT_DEVELOPMENT),
    DESIGN(Description.DESIGN),
    CULTURAL_ACTIVITY(Description.CULTURAL_ACTIVITY),
    MUSIC(Description.MUSIC),
    TRAVEL(Description.TRAVEL),
    VOLUNTEERING(Description.VOLUNTEERING),
    SPORTS(Description.SPORTS),
    PETS(Description.PETS),
    SOCIALIZING(Description.SOCIALIZING),
    CLASSES(Description.CLASSES),
    FOREIGN_LANGUAGES(Description.FOREIGN_LANGUAGES),
    RESTAURANTS(Description.RESTAURANTS),
    COOKING(Description.COOKING),
    FINANCE(Description.FINANCE),
    OTHERS(Description.OTHERS);

    private final String description;

    Interest(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String IT_DEVELOPMENT = "IT/개발";
        public static final String DESIGN = "디자인";
        public static final String CULTURAL_ACTIVITY = "문화활동";
        public static final String MUSIC = "음악";
        public static final String TRAVEL = "여행";
        public static final String VOLUNTEERING = "봉사활동";
        public static final String SPORTS = "운동";
        public static final String PETS = "반려동물";
        public static final String SOCIALIZING = "사교";
        public static final String CLASSES = "수업";
        public static final String FOREIGN_LANGUAGES = "외국어";
        public static final String RESTAURANTS = "맛집";
        public static final String COOKING = "요리";
        public static final String FINANCE = "금융";
        public static final String OTHERS = "기타";
    }
}
