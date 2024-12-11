package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Plant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plant_id")
    private int plantId;

    @Enumerated(EnumType.STRING)
    @Column(name = "plant_type", nullable = false)
    private PlantType plantType;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Column(name = "max_sunlight", nullable = false)
    private float maxSunlight;

    @Column(name = "sale_price", nullable = false)
    private int salePrice;

    @Column(name = "plant_description", columnDefinition = "TEXT")
    private String plantDescription;

    @Column(name = "level1_image")
    private String level1Image;

    @Column(name = "level2_image")
    private String level2Image;

    @Column(name = "level3_image")
    private String level3Image;

    @Column(name = "max_image")
    private String maxImage;

    @Column(name = "dead_image")
    private String deadImage;

    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }

    public enum PlantType {
        TOMATO,
        BEAN,
        SUNFLOWER,
        TULIP,
        ROSE,
        LEMON,
        APPLE
    }
}