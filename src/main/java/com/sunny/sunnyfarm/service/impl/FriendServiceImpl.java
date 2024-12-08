package com.sunny.sunnyfarm.service.impl;

import com.sunny.sunnyfarm.dto.FriendDto;
import com.sunny.sunnyfarm.entity.Friend;
import com.sunny.sunnyfarm.entity.User;
import com.sunny.sunnyfarm.repository.FriendRepository;
import com.sunny.sunnyfarm.repository.UserRepository;
import com.sunny.sunnyfarm.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendServiceImpl(FriendRepository friendRepository, UserRepository userRepository, UserRepository userRepository1) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository1;
    }

    @Override
    public Map<String, List<FriendDto>> getFriendList(int userId) {
        List<Friend> friendList = friendRepository.findByUserId(userId);
        // 상태별로 분리
        List<FriendDto> pendingFriends = friendList.stream()
                .filter(friend -> friend.getStatus() == Friend.FriendStatus.PENDING)
                .map(friend -> new FriendDto(
                        friend.getFriendUser().getUserId(),
                        friend.getFriendUser().getUserName(),
                        friend.getFriendUser().getProfilePicture())
                ).collect(Collectors.toList());

        List<FriendDto> acceptedFriends = friendList.stream()
                .filter(friend -> friend.getStatus() == Friend.FriendStatus.ACCEPTED)
                .map(friend -> new FriendDto(
                        friend.getFriendUser().getUserId(),
                        friend.getFriendUser().getUserName(),
                        friend.getFriendUser().getProfilePicture())
                ).collect(Collectors.toList());

        // JSON 응답 형식에 맞게 구성
        Map<String, List<FriendDto>> response = new HashMap<>();
        response.put("PENDING", pendingFriends);
        response.put("ACCEPTED", acceptedFriends);

        return response;
    }


    @Override
    public List<FriendDto> searchFriend(String userName) {
        List<User> searchList = friendRepository.findByUserName(userName);
        return searchList.stream()
                .map(friend -> new FriendDto(
                        friend.getUserId(),
                        friend.getUserName(),
                        friend.getProfilePicture())
                ).collect(Collectors.toList());
    }

    private Friend.FriendStatus isFriend(int userId, int friendUserId) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userId, friendUserId);
        if (friend == null) {
            return null;
        } else {
            return friend.getStatus();
        }
    }

    @Override
    public ResponseEntity<String> sendFriendRequest(int userId, int friendUserId) {
        Friend.FriendStatus is_friend = isFriend(userId, friendUserId);
        if (is_friend != null) { //내가 걸었음
            if (is_friend == Friend.FriendStatus.PENDING) {
                return ResponseEntity.status(400).body("이미 요청을 건 친구입니다.");
            }
            else {
                return ResponseEntity.status(400).body("이미 친구입니다.");
            }
        }
        else {
            is_friend = isFriend(friendUserId, userId);
            if (is_friend != null) { //내가 받았음
                return ResponseEntity.status(400).body("이미 요청을 받은 친구입니다.");
            }
            else {
                try {
                    Friend sendFriend = new Friend(
                            0,
                            userRepository.getById(userId),
                            userRepository.getReferenceById(friendUserId),
                            Friend.FriendStatus.PENDING
                    );
                    friendRepository.save(sendFriend);
                    return ResponseEntity.ok("친구 요청을 보냈습니다.");
                } catch (Exception e) {
                    return ResponseEntity.status(400).body("없는 유저입니다.");
                }
            }
        }
    }

    @Override
    public ResponseEntity<String> acceptFriendRequest(int userId, int friendUserId) {
        try {
            Friend userToFriend = new Friend(
                    0,
                    userRepository.getById(userId),
                    userRepository.getReferenceById(friendUserId),
                    Friend.FriendStatus.ACCEPTED
            );

            friendRepository.updateStatus(friendUserId, userId, Friend.FriendStatus.ACCEPTED);
            friendRepository.save(userToFriend);

            return ResponseEntity.ok("친구가 추가되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("친구 추가에 실패하였습니다.");
        }
    }


    @Override
    public ResponseEntity<String> rejectFriendRequest(int userId, int friendUserId) {
        try {
            friendRepository.deleteByUserIdAndFriendUserId(friendUserId, userId);
            return ResponseEntity.ok("친구 요청을 거절했습니다.");
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(400).body("친구 요청 거절에 실패하였습니다.");
        }
    }

    @Override
    public ResponseEntity<String> waterFriendPlant(int userId, int friendUserId, int plantId) {
        return null;
    }
}
