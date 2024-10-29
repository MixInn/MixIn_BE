package com.sparta.mixin.domain.meetapplication.entity;

public enum ResultEnum {
    ACCEPTED("수락"),
    REJECTED("거절"),
    PENDING("대기");

    private String status;

    ResultEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
