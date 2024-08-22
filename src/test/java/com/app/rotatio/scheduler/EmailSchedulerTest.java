package com.app.rotatio.scheduler;

import com.app.rotatio.config.AdminConfig;
import com.app.rotatio.domain.Mail;
import com.app.rotatio.domain.User;
import com.app.rotatio.repository.UserRepository;
import com.app.rotatio.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailSchedulerTest {

    @Mock
    private AdminConfig adminConfig;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailScheduler emailScheduler;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .email("test@example.com")
                .userStatus("ENABLED")
                .build();
    }

    @Test
    void shouldSendInformationEmail() {
        //Given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(adminConfig.getMail()).thenReturn("admin@example.com");
        //When
        emailScheduler.sendInformationEmail();
        //Then
        verify(emailService, times(1)).send(any(Mail.class));
    }

    @Test
    void shouldSendEmailToAllUsers() {
        //Given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(adminConfig.getMail()).thenReturn("admin@example.com");
        //When
        emailScheduler.sentEmailToAllUsers();
        //Then
        verify(emailService, times(1)).send(any(Mail.class));
    }

    @Test
    void shouldNotSendEmailIfNoUsers() {
        //Given
        when(userRepository.findAll()).thenReturn(List.of());
        //When
        emailScheduler.sentEmailToAllUsers();
        //Then
        verify(emailService, never()).send(any(Mail.class));
        verify(userRepository, times(1)).findAll();
    }
}