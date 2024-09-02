package com.sparta.mixin.domain.image.entity;

import com.sparta.mixin.domain.community.meetcommunity.entity.MeetCommunity;
import com.sparta.mixin.domain.community.publiccommunity.entity.PublicCommunity;
import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "entityMappingImage")
public class EntityMappingImage extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EntityType entityType;

    private Long entityId;

    @ManyToOne
    @JoinColumn(name = "publicCommunity_id", nullable = false)
    private PublicCommunity publicCommunity;

    @ManyToOne
    @JoinColumn(name = "meetCommunity_id", nullable = false)
    private MeetCommunity meetCommunity;

    // Getters and setters
}