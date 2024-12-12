package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PlantBook")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plantbook_id")
    private int plantbookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "plantbook_description", columnDefinition = "TEXT")
    private String plantbookDescription;

    @Column(name = "plantbook_image")
    private String plantbookImage;
}