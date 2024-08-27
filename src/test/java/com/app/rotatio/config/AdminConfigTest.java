package com.app.rotatio.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AdminConfigTest {

    @Autowired
    private AdminConfig adminConfig;

    @Test
    void shouldReturnAdminEmail() {
        //Given
        //When
        String mail = adminConfig.getMail();
        //Then
        assertNotNull(mail);
        assertTrue(mail.contains("rotatio"));
    }

    @Test
    void shouldReturnEmailContainsAtSign() {
        //Given
        //When
        String mail = adminConfig.getMail();
        //Then
        assertTrue(mail.contains("@"));
    }
}