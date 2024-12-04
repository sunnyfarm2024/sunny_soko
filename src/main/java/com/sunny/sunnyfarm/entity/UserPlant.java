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

    @Column(name = "plant_name", nullable = true)
    private String plantName;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", nullable = false)
    private Difficulty difficulty;

    @Enumerated(EnumType.STRING)
    @Column(name = "growth_stage", nullable = false)
    private GrowthStage growthStage;

    @Column(name = "growth_progress", nullable = false)
    private float growthProgress;

    @Column(name = "sunlight_hours", nullable = false)
    private float sunlightHours;

    @Column(name = "water_level", nullable = false)
    private int waterLevel;

    @Column(name = "lives_left", nullable = false)
    private int livesLeft;

    @Column(name = "last_watered_at", nullable = false)
    private LocalDateTime lastWateredAt;

    @Column(name = "fertilizer_ends_at", nullable = true)
    private LocalDateTime fertilizerEndsAt;

    public enum Difficulty {
        ONE(1),
        TWO(2),
        THREE(3);

        private final int value;

        Difficulty(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum GrowthStage {
        ONE("1"),
        TWO("2"),
        THREE("3"),
        MAX("Max");

        private final String value;

        GrowthStage(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}