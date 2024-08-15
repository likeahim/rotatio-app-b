package com.app.rotatio.mapper;

import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.UserDto;
import com.app.rotatio.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
                .isEnabled(userDto.isEnabled())
                .plannedDays(workingDayService.longToWorkingDaysList(userDto.plannedDays()))
                .build();
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastname(),
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                workingDayService.workingDaysToLongList(user.getPlannedDays())
        );
    }

    public List<UserDto> mapToUserDtoList(List<User> users) {
        return users.stream()
                .map(this::mapToUserDto)
                .toList();
    }
}
