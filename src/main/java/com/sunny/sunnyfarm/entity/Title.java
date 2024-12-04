package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Title")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "title_id")
    private int titleId;

    @Column(name = "title_name", nullable = false, length = 50)
    private String titleName;

    @Column(name = "title_requirement", nullable = false)
    private int titleRequirement;
}