package com.sparta.mixin.domain.profile.entity;

public enum ParticipationType {

    LEADER(Description.LEADER),
    MOOD_MAKER(Description.MOOD_MAKER),
    CALM_TYPE(Description.CALM_TYPE),
    IAM_EVERYTHING_OK(Description.IAM_EVERYTHING_OK);

    private final String description;

    ParticipationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String LEADER = "리더";
        public static final String MOOD_MAKER = "분위기메이커";
        public static final String CALM_TYPE = "차분형";
        public static final String IAM_EVERYTHING_OK = "다좋아";
    }
}

