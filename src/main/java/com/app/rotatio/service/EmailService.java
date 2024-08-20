package com.app.rotatio.service;

import com.app.rotatio.config.AdminConfig;
import com.app.rotatio.domain.Mail;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.Worker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final AdminConfig adminConfig;

    public void send(final Mail mail) {
        try {
            SimpleMailMessage message = createMailMessage(mail);
            javaMailSender.send(message);
            log.info("Email sent to " + mail.getTo());
        } catch (MailException e) {
            log.error("Failed to process email sending: " + e.getMessage(), e);
        }
    }

    private SimpleMailMessage createMailMessage(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getTo());
        message.setSubject(mail.getSubject());
        message.setText(mail.getBody());
        message.setFrom(mail.getFrom());
        Optional<String> optionalCc = Optional.ofNullable(mail.getToCc());
        optionalCc.ifPresent(message::setCc);
        return message;
    }

    public void sendNewUserMessage(final User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminConfig.getMail());
            message.setText("New User with mail: " + user.getEmail() + " added to the list");
            message.setSubject("NEW USER");
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("Failed to process email sending: " + e.getMessage(), e);
        }
    }
}
