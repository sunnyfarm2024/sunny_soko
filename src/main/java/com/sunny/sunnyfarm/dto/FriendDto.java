package com.sunny.sunnyfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendDto {
    private int userId;
    private String userName;
    private String profile;
}