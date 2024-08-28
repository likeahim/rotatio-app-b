package com.app.rotatio.controller;

import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.UserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import com.app.rotatio.facade.UserFacade;
import com.app.rotatio.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/rotatio/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserFacade userFacade;
    private final UserMapper mapper;

    @SneakyThrows
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> register(@Validated @RequestBody UserDto userDto) {
        User user = mapper.mapToUser(userDto);
        User registeredUser = userFacade.registerAndSaveUser(user);
        return ResponseEntity.ok(mapper.mapToUserDto(registeredUser));
    }

    @SneakyThrows
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> login(@Validated @RequestBody BackendlessLoginUserDto userDto) {
        BackendlessLoginUser backendlessLoginUser = userFacade.mapToBackendlessUser(userDto);
        User user = userFacade.loginUser(backendlessLoginUser);
        return ResponseEntity.ok(mapper.mapToUserDto(user));
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<UserDto> update(@Validated @RequestBody UserDto userDto) {
        User user = mapper.mapToUser(userDto);
        User updated = userFacade.updateUser(user);
        return ResponseEntity.ok(mapper.mapToUserDto(updated));
    }

    @SneakyThrows
    @DeleteMapping(value = "/{objectId}")
    public ResponseEntity<Object> delete(@PathVariable String objectId) {
        return ResponseEntity.ok(mapper.mapToUserDto(userFacade.deleteUser(objectId)));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAll() {
        List<User> allUsers = userFacade.getAllUsers();
        return ResponseEntity.ok(mapper.mapToUserDtoList(allUsers));
    }

    @SneakyThrows
    @GetMapping(value = "/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable Long userId) {
        userFacade.logout(userId);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/password")
    public ResponseEntity<Void> restorePassword(@RequestParam String email) {
        userFacade.restorePasswordByUserMail(email);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "/{userId}")
    public ResponseEntity<UserDto> getUser(@Validated @PathVariable Long userId) {
        User user = userFacade.getUserById(userId);
        return ResponseEntity.ok(mapper.mapToUserDto(user));
    }

    @SneakyThrows
    @GetMapping(value = "/byEmail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        User userByEmail = userFacade.fetchUserByEmail(email);
        return ResponseEntity.ok(mapper.mapToUserDto(userByEmail));
    }
}
