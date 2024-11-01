package com.sparta.mixin.domain.profile.entity;

public enum Personality {

    ARGUMENTATIVE(Description.ARGUMENTATIVE),
    PEACE_LOVING(Description.PEACE_LOVING),
    SHY(Description.SHY),
    ELOQUENT(Description.ELOQUENT),
    SERIOUS(Description.SERIOUS),
    PLAYFUL(Description.PLAYFUL),
    PLANNING(Description.PLANNING),
    IMPROVISATIONAL(Description.IMPROVISATIONAL),
    EASYGOING(Description.EASYGOING),
    IMPATIENT(Description.IMPATIENT),
    PARTY_LOVER(Description.PARTY_LOVER),
    SMALL_GROUP_LOVER(Description.SMALL_GROUP_LOVER),
    DECISIVE(Description.DECISIVE),
    INDECISIVE(Description.INDECISIVE),
    EMOTIONAL(Description.EMOTIONAL),
    RATIONAL(Description.RATIONAL);

    private final String description;

    Personality(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public static class Description {
        public static final String ARGUMENTATIVE = "논쟁을 좋아하는";
        public static final String PEACE_LOVING = "평화를 좋아하는";
        public static final String SHY = "수줍음을 타는";
        public static final String ELOQUENT = "말주변이 좋은";
        public static final String SERIOUS = "진지한";
        public static final String PLAYFUL = "장난끼가 많은";
        public static final String PLANNING = "계획적인";
        public static final String IMPROVISATIONAL = "즉흥적인";
        public static final String EASYGOING = "느긋한";
        public static final String IMPATIENT = "성격이 급한";
        public static final String PARTY_LOVER = "파티를 좋아하는";
        public static final String SMALL_GROUP_LOVER = "소수모임을 좋아하는";
        public static final String DECISIVE = "단호한";
        public static final String INDECISIVE = "우유부단한";
        public static final String EMOTIONAL = "감성적인";
        public static final String RATIONAL = "이성적인";
    }
}
