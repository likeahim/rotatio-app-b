package com.app.rotatio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/rotatio/users")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {
}
