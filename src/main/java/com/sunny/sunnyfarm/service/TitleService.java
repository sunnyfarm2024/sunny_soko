package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.UserTitleDto;

import java.util.List;

public interface TitleService {
    List<UserTitleDto> getTitleList(int userId);
    boolean changeTitle(int titleId, int userId);
    boolean archiveTitle(int plantId, int userId);
}