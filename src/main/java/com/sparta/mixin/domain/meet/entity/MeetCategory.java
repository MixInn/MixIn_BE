package com.sparta.mixin.domain.meet.entity;

public enum MeetCategory {
    IT_DEVELOPMENT("IT/개발"),       // IT/개발
    DESIGN("디자인"),               // 디자인
    CULTURE_ACTIVITY("문화활동"),     // 문화활동
    MUSIC("음악"),                  // 음악
    TRAVEL("여행"),                 // 여행
    VOLUNTEER("봉사활동"),            // 봉사활동
    SPORTS("운동"),                 // 운동
    PETS("반려동물"),                // 반려동물
    SOCIAL("사교"),                 // 사교
    CLASS("수업"),                  // 수업
    FOREIGN_LANGUAGE("외국어"),       // 외국어
    FOOD_SPOTS("맛집"),              // 맛집
    COOKING("요리"),                // 요리
    FINANCE("금융"),                 // 금융
    OTHERS("기타");                 // 기타

    private final String categoryName;

    // Constructor
    MeetCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    // Getter for the category name
    public String getCategoryName() {
        return categoryName;
    }

    // string값을 enum으로 변환해주는 함수
    public static MeetCategory fromString(String type) {
        for (MeetCategory meetCategory : MeetCategory.values()) {
            if (meetCategory.getCategoryName().equalsIgnoreCase(type)) {
                return meetCategory;
            }
        }
        throw new IllegalArgumentException("Unknown meet type: " + type);
    }
}
