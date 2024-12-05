package com.sunny.sunnyfarm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ShopDto {
    private String ItemName;
    private String ItemDescription;
    private int price;
    private String ItemCategory;
}
