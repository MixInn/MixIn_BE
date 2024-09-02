package com.sparta.mixin.domain.image.entity;

import com.sparta.mixin.global.Timestamped;
import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "entitymappingImage_id", nullable = false)
    private EntityMappingImage entityMappingImage;

    // Getters and setters
}
