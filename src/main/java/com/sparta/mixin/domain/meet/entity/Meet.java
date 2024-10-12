package com.sparta.mixin.domain.meet.entity;

import com.sparta.mixin.domain.meet.dto.MeetRequestDto;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Meet")
@NoArgsConstructor
public class Meet extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MeetType type;

    @Enumerated(EnumType.STRING)
    private MeetCategory category;

    private String image;
    private String name;
    private String info;
    private String rule;
    private String tag;

    // Getters and setters
    @Builder
    public Meet(MeetType type,MeetCategory meetCategory, String image, String name, String info, String rule, String tag) {
        this.type = type;
        this.category = meetCategory;
        this.image = image;
        this.name = name;
        this.info = info;
        this.rule = rule;
        this.tag = tag;

    }

    public void updateMeet(MeetRequestDto meetRequestDto) {
        if (meetRequestDto.getType() != null) {
            this.type = MeetType.fromString(meetRequestDto.getType());
        }
        if (meetRequestDto.getCategory() != null) {
            this.category = MeetCategory.fromString(meetRequestDto.getCategory());
        }
        if (meetRequestDto.getImage() != null) {
            this.image = meetRequestDto.getImage();
        }
        if (meetRequestDto.getName() != null) {
            this.name = meetRequestDto.getName();
        }
        if (meetRequestDto.getInfo() != null) {
            this.info = meetRequestDto.getInfo();
        }
        if (meetRequestDto.getRule() != null) {
            this.rule = meetRequestDto.getRule();
        }
        if (meetRequestDto.getTag() != null) {
            this.tag = meetRequestDto.getTag();
        }
    }
}