package com.app.rotatio.scheduler;

import com.app.rotatio.config.AdminConfig;
import com.app.rotatio.domain.Mail;
import com.app.rotatio.domain.User;
import com.app.rotatio.repository.UserRepository;
import com.app.rotatio.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailScheduler {

    private static final String SUBJECT_USERS = "Enabled users in Rotatio App";
    private static final String SUBJECT_PLANS = "Unplanned days";
    private final AdminConfig adminConfig;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 1 20 * * *")
    public void sendInformationEmail() {
        long size = userRepository.findAll().stream()
                .filter(u -> u.getUserStatus().equals("ENABLED"))
                .count();
        String matter = size == 1 ? "user" : "users";
        Mail mail = Mail.builder()
                .from(adminConfig.getMail())
                .to(adminConfig.getMail())
                .subject(SUBJECT_USERS)
                .body("Number of users with status ENABLED: " + size + " " + matter)
                .build();
        emailService.send(mail);
    }

    @Scheduled(cron = "0 0 20 * * *")
    @Transactional
    public void sentEmailToAllUsers() {
        List<User> all = userRepository.findAll();
        if (all.isEmpty()) {
            log.info("No users in Rotatio App");
        } else {
            for (User user : all) {
                long count = user.getPlannedDays().stream()
                        .filter(d -> !d.isPlanned())
                        .count();
                emailService.send(Mail.builder()
                        .from(adminConfig.getMail())
                        .to(user.getEmail())
                        .subject(SUBJECT_PLANS)
                        .body("You have still unplanned days: " + count)
                        .build()
                );
            }
        }
    }
}
