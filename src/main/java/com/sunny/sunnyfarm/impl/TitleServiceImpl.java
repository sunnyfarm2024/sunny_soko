package com.sunny.sunnyfarm.impl;

import com.sunny.sunnyfarm.dto.TitleDto;
import com.sunny.sunnyfarm.entity.Title;
import com.sunny.sunnyfarm.entity.UserTitle;
import com.sunny.sunnyfarm.repository.TitleRepository;
import com.sunny.sunnyfarm.service.TitleService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TitleServiceImpl implements TitleService {

    private static final Logger log = LoggerFactory.getLogger(TitleServiceImpl.class);
    private final TitleRepository titleRepository;

    public TitleServiceImpl(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    @Override
    public ResponseEntity<List<TitleDto>> getTitleList(int userId) {
        // Fetch UserTitle entities
        List<UserTitle> userTitles = titleRepository.findByUserId(userId);

        // Convert UserTitle to TitleDto
        List<TitleDto> titleDtos = userTitles.stream()
                .map(userTitle -> {
                    Title title = userTitle.getTitle();

                    return new TitleDto(
                            title.getTitleId(),
                            title.getTitleName(),
                            userTitle.isActive()
                    );
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(titleDtos);
    }

    @Override
    @Transactional
    public boolean changeTitle(int titleId, int userId) {

        log.debug("changeTitle called with titleId: {}, userId: {}", titleId, userId);

        int selectedTitleUpdated = titleRepository.updateSelectedTitle(titleId, userId);

        int isActiveUpdated = titleRepository.updateIsActive(titleId, userId);

        return selectedTitleUpdated > 0 && isActiveUpdated > 0;
    }

    @Override
    @Transactional
    public boolean archiveTitle(int plantId, int userId) {
        System.out.println("changeProgress called with plantId: " + plantId + ", userId: " + userId);

        UserTitle title = titleRepository.findByTitleId(userId, plantId);
        if (title != null) {
            System.out.println("User ID: " + title.getUser().getUserId());
            System.out.println("Title Requirement: " + title.getTitle().getTitleRequirement());
            System.out.println("Title ID: " + title.getTitle().getTitleId());
            System.out.println("Title Progress: " + title.getTitleProgress());
            System.out.println("Is Title Completed: " + title.isTitleCompleted());
        } else {
            System.out.println("No UserTitle found for userId: " + userId + " and plantId: " + plantId);
        }

        if (title != null && title.isTitleCompleted()) return false;

        updateMasterTitle(userId, plantId);

        switch (plantId) {
            case 1, 2 -> updateFarmerTitle(userId, 9);
            case 3, 4, 5 -> updateFarmerTitle(userId, 10);
            case 6, 7 -> updateFarmerTitle(userId, 11);
            default -> throw new IllegalArgumentException("Invalid plantId: " + plantId);
        }

        return true;
    }

    @Transactional
    protected void updateMasterTitle(int userId, int plantId) {
        titleRepository.updateMasterTitle(userId, plantId);
    }

    @Transactional
    protected void updateFarmerTitle(int userId, int titleId) {
        titleRepository.updateFarmerTitle(userId, titleId);
    }
}
