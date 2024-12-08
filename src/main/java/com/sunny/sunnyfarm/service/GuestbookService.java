package com.sunny.sunnyfarm.service;

import com.sunny.sunnyfarm.dto.GuestbookDto;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface GuestbookService {
    List<GuestbookDto> getGuestbook(int userId);
    void writeGuestbook(int userId, int friendUserId, String content);
    void checkRead(int userId);
}
