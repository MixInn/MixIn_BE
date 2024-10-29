package com.sparta.mixin.domain.meet.entity;

public enum MeetType {
    CLUB("동아리"),         // "동아리" in Korean
    STUDY("스터디"),        // "스터디" in Korean
    PROJECT("프로젝트"),    // "프로젝트" in Korean
    LIGHTNING("번개");      // "번개" in Korean

    private final String typeName;

    MeetType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    // string값을 enum으로 변환해주는 함수
    public static MeetType fromString(String type) {
        for (MeetType meetType : MeetType.values()) {
            if (meetType.getTypeName().equalsIgnoreCase(type)) {
                return meetType;
            }
        }
        throw new IllegalArgumentException("Unknown meet type: " + type);
    }
}
