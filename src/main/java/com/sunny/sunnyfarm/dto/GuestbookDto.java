package com.sunny.sunnyfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GuestbookDto {
    private int authorId;
    private String content;
    private LocalDateTime createdAt;
}
