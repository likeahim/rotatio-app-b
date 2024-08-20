package com.app.rotatio.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AdminConfig {

    @Value("${spring.mail.username}")
    private String mail;
    private static final String GREETINGS = "Sincerely,\nRotatio Team";
}
