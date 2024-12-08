package com.sunny.sunnyfarm.service.impl;

import com.sunny.sunnyfarm.dto.GuestbookDto;
import com.sunny.sunnyfarm.entity.GuestBook;
import com.sunny.sunnyfarm.repository.GuestbookRepository;
import com.sunny.sunnyfarm.repository.UserRepository;
import com.sunny.sunnyfarm.service.GuestbookService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GuestbookImpl implements GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;

    public GuestbookImpl(GuestbookRepository guestbookRepository, UserRepository userRepository) {
        this.guestbookRepository = guestbookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GuestbookDto> getGuestbook(int userId) {
        List<GuestBook> guestBookList = guestbookRepository.findByUserId(userId);
        return guestBookList.stream()
                .map(guestbook -> new GuestbookDto(
                        guestbook.getAuthor().getUserId(),
                        guestbook.getContent(),
                        guestbook.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public void writeGuestbook(int userId, int friendUserId, String content) {
        GuestBook guestbook = new GuestBook(
                0,
                userRepository.getById(friendUserId), //텃밭 주인
                userRepository.getById(userId), //글쓴이
                content,
                false,
                LocalDateTime.now()
        );
        guestbookRepository.save(guestbook);
    }

    @Override
    public void checkRead(int userId) {
        guestbookRepository.updateIsReadByUserId(userId);
    }
}
