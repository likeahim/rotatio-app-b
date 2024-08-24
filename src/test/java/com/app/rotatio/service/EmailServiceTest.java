package com.app.rotatio.service;

import com.app.rotatio.config.AdminConfig;
import com.app.rotatio.domain.Mail;
import com.app.rotatio.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private AdminConfig adminConfig;

    @InjectMocks
    private EmailService emailService;

    private Mail mail;
    private User user;

    @BeforeEach
    void setUp() {
        mail = Mail.builder()
                .from("from@example.com")
                .to("test@example.com")
                .subject("Test Subject")
                .body("Test Body")
                .build();
        user = User.builder()
                        .email("user@example.com")
                                .build();
    }

    @Test
    void shouldSendEmailSuccessfully() {
        //When
        assertDoesNotThrow(() -> emailService.send(mail));
        //Then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(javaMailSender);
    }

    @Test
    void shouldHandleMailExceptionDuringSend() {
        //Given
        doThrow(new MailException("Test exception") {}).when(javaMailSender).send(any(SimpleMailMessage.class));
        //When
        emailService.send(mail);
        //Then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(javaMailSender);
    }

    @Test
    void shouldCreateMailMessageSuccessfully() {
        //Given
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setTo(mail.getTo());
        expectedMessage.setSubject(mail.getSubject());
        expectedMessage.setText(mail.getBody());
        expectedMessage.setFrom(mail.getFrom());
        //When
        emailService.send(mail);
        //Then
        verify(javaMailSender, times(1)).send(expectedMessage);
        assertDoesNotThrow(() -> emailService.send(mail));
    }

    @Test
    void shouldSendNewUserMessageSuccessfully() {
        //Given
        when(adminConfig.getMail()).thenReturn("admin@example.com");
        //When
        assertDoesNotThrow(() -> emailService.sendNewUserMessage(user));
        //Then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(adminConfig, times(1)).getMail();
        verifyNoMoreInteractions(javaMailSender);
    }

    @Test
    void shouldHandleMailExceptionDuringSendNewUserMessage() {
        //Given
        when(adminConfig.getMail()).thenReturn("admin@example.com");
        doThrow(new MailException("Test exception") {}).when(javaMailSender).send(any(SimpleMailMessage.class));
        //When
        emailService.sendNewUserMessage(user);
        //Then
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(adminConfig, times(1)).getMail();
        verifyNoMoreInteractions(javaMailSender);
    }
}