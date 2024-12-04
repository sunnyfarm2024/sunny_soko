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