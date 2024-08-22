package com.app.rotatio.controller;

import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.UserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import com.app.rotatio.mapper.UserMapper;
import com.app.rotatio.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/rotatio/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @SneakyThrows
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> register(@Validated @RequestBody UserDto userDto) {
        User user = mapper.mapToUser(userDto);
        User registeredUser = userService.registerAndSaveUser(user);
        return ResponseEntity.ok(mapper.mapToUserDto(registeredUser));
    }

    @SneakyThrows
    @DeleteMapping(value = "{objectId}")
    public ResponseEntity<Object> delete(@PathVariable String objectId) {
        return ResponseEntity.ok(mapper.mapToUserDto(userService.delete(objectId)));
    }

    @SneakyThrows
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> login(@Validated @RequestBody BackendlessLoginUserDto userDto) {
        BackendlessLoginUser backendlessLoginUser = userService.mapToBackendlessUser(userDto);
        User user = userService.logAndSaveUser(backendlessLoginUser);
        return ResponseEntity.ok(mapper.mapToUserDto(user));
    }

    @SneakyThrows
    @GetMapping(value = "/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable Long userId) {
        userService.logout(userId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/password/{userId}")
    public ResponseEntity<Void> restorePassword(@PathVariable Long userId) {
        userService.restorePasswordByUserMail(userId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(mapper.mapToUserDto(user));
    }
}
