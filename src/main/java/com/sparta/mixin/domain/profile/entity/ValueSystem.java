package com.sparta.mixin.domain.profile.entity;

public enum ValueSystem {

    COMMUNICATION(ValueDescription.COMMUNICATION),
    PASSION(ValueDescription.PASSION),
    PROMISE(ValueDescription.PROMISE);

    private final String description;

    ValueSystem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class ValueDescription {
        public static final String COMMUNICATION = "소통";
        public static final String PASSION = "열정";
        public static final String PROMISE = "약속";
    }
}

