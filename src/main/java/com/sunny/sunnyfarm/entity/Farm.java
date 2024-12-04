package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Farm")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Farm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "farm_id")
    private int farmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "farm_description", columnDefinition = "TEXT")
    private String farmDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "left_plant_id")
    private UserPlant leftPlant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_plant_id")
    private UserPlant centerPlant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "right_plant_id")
    private UserPlant rightPlant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sign_id")
    private Shop sign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "corner_id")
    private Shop corner;

    @Column(name = "gnome_ends_at")
    private LocalDateTime gnomeEndsAt;
}