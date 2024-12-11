package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserPlant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_plant_id")
    private int userPlantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Column(name = "plant_name")
    private String plantName;

    @Enumerated(EnumType.STRING)
    @Column(name = "growth_stage", nullable = false)
    private GrowthStage growthStage = GrowthStage.LEVEL1;

    @Column(name = "growth_progress", nullable = false)
    private float growthProgress = 0;

    @Column(name = "sunlight_hours", nullable = false)
    private float sunlightHours = 0;

    @Column(name = "water_level", nullable = false)
    private int waterLevel = 5;

    @Column(name = "lives_left", nullable = false)
    private int livesLeft = 3;

    @Column(name = "last_watered_at", nullable = false)
    private LocalDateTime lastWateredAt;

    @Column(name = "fertilizer_ends_at")
    private LocalDateTime fertilizerEndsAt;

    public enum GrowthStage {
        LEVEL1,
        LEVEL2,
        LEVEL3,
        MAX
    }
}
