package com.sunny.sunnyfarm.dto;

import com.sunny.sunnyfarm.entity.Title;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTitleDto {
    private int userTitleId;        // UserTitle ID
    private int userId;             // User ID
    private int titleProgress;      // Title Progress
    private boolean isTitleCompleted; // Title Completed Status
    private boolean isActive;       // Active Status
    private Title title;            // Embedded Title Object
}
