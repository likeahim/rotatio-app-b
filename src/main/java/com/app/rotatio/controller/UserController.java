package com.app.rotatio.controller;

import com.app.rotatio.api.backendless.client.BackendlessClient;
import com.app.rotatio.domain.dto.backendless.BackendlessUserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToRegisterDto;
import com.app.rotatio.mapper.BackendlessMapper;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/rotatio/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final BackendlessClient backendlessClient;
    private final BackendlessMapper mapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BackendlessUserDto> register(@RequestBody BackendlessUserDto userDto) {
        return ResponseEntity.ok(backendlessClient.registerUser(userDto));
    }
}
