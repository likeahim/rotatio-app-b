package com.app.rotatio.mapper;

import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.UserDto;
import com.app.rotatio.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMapper {

    private final WorkingDayService workingDayService;

    public User mapToUser(UserDto userDto) {
        return User.builder()
                .id(userDto.id())
                .firstName(userDto.firstName())
                .lastname(userDto.lastname())
                .email(userDto.email())
                .password(userDto.password())
                .userStatus(userDto.userStatus())
                .objectId(userDto.objectId())
                .userToken(userDto.userToken() != null ? userDto.userToken() : null)
                .plannedDays(new ArrayList<>())
                .build();
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.getUserStatus(),
                user.getObjectId(),
                user.getUserToken() != null ? user.getUserToken() : null,
                workingDayService.workingDaysToLongList(user.getPlannedDays()) != null ?
                        workingDayService.workingDaysToLongList(user.getPlannedDays()) : new ArrayList<>()
        );
    }

    public List<UserDto> mapToUserDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }
}
