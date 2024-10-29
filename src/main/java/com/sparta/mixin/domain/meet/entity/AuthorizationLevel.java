package com.sparta.mixin.domain.meet.entity;

public enum AuthorizationLevel {
    LEADER("리더"),
    SUBLEADER("부리더"),
    MEMBER("멤버");

    private String status;

    AuthorizationLevel(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
