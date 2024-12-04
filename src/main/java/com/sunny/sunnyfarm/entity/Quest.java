package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Quest")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private int questId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private QuestType type;

    @Column(name = "quest_description", columnDefinition = "TEXT")
    private String questDescription;

    @Column(name = "reward", nullable = false)
    private int reward;

    @Column(name = "quest_requirement", nullable = false)
    private int questRequirement;

    public enum QuestType {
        DAILY,
        LONGTERM
    }
}