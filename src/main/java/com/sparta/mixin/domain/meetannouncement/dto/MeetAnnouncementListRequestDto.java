package com.sparta.mixin.domain.meetannouncement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeetAnnouncementListRequestDto {
    private int page;
    private int size;
    private String meetType;
    private String category;
    private String tags;
    private String meetName;
    private String sortType;

    public MeetAnnouncementListRequestDto(String meetType, String category, String tags, String meetName,String sortType, int page, int size) {
        this.meetType = meetType;
        this.category = category;
        this.tags = tags;
        this.meetName = meetName;
        this.sortType = sortType;
        this.page = page;
        this.size = size;
    }
}
