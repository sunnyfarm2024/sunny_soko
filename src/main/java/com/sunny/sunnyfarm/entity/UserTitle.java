package com.sunny.sunnyfarm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usertitle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_title_id")
    private int userTitleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", nullable = false)
    private Title title;

    @Column(name = "title_progress", nullable = false)
    private int titleProgress;

    @Column(name = "is_title_completed", nullable = false)
    private boolean isTitleCompleted;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

}
