package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.TitleDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TitleService {
    ResponseEntity<List<TitleDto>> getTitleList(int userId);
    boolean changeTitle(int titleId, int userId);
    boolean archiveTitle(int plantId, int userId);
}