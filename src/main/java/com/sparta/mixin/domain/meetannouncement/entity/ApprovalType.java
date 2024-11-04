package com.sparta.mixin.domain.meetannouncement.entity;

public enum ApprovalType {
    OPEN("자유롭게 가입"),         // 자유롭게 가입
    APPROVAL_REQUIRED("승인 후 가입"); // 승인 후 가입

    private final String description;

    ApprovalType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // String 값을 ApprovalType enum으로 변환하는 메서드
    public static ApprovalType fromString(String value) {
        for (ApprovalType type : ApprovalType.values()) {
            if (type.getDescription().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown approval type: " + value);
    }
}

