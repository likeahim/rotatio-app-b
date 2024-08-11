package com.app.rotatio.domain;

import com.app.rotatio.repository.UserRepository;
import com.app.rotatio.repository.WorkingDayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class UserEntityTests {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
                .firstName("John")
                .lastname("Tester")
                .email("john.tester@test.com")
                .password("testing123")
                .build();
    }

    @Nested
    class UserRepositorySimpleTests {

        @Test
        void shouldCreateUser() {
            //Given
            Long beforeSave = user.getId();
            //When
            userRepository.save(user);
            //Then
            assertNotNull(user.getId());
            assertNotEquals(beforeSave, user.getId());
            assertTrue(userRepository.existsById(user.getId()));
        }

        @Test
        void shouldThrowException() {
            //Given
            userRepository.save(user);
            User secondUser = User.builder()
                    .email("john.tester@test.com")
                    .password("testing123")
                    .build();
            //When&Then
            assertThrows(Exception.class, () -> userRepository.saveAndFlush(secondUser));
        }

        @Test
        void shouldUpdateUser() {
            //Given
            userRepository.save(user);
            user.setLastname("Updater");
            user.setEnabled(false);
            user.setPassword("update123");
            //When
            User savedUser = userRepository.save(user);
            //Then
            assertNotNull(user.getId());
            assertEquals("Updater", savedUser.getLastname());
            assertEquals("update123", savedUser.getPassword());
            assertFalse(savedUser.isEnabled());
        }

        @Test
        void shouldFetchUserById() {
            //Given
            userRepository.save(user);
            Long userId = user.getId();
            //When
            User foundUser = userRepository.findById(userId).orElseThrow();
            //Then
            assertNotNull(foundUser);
            assertEquals("John", foundUser.getFirstName());
        }

        @Test
        void shouldFetchAllUsers() {
            //Given
            User secondUser = User.builder()
                    .firstName("Mark")
                    .lastname("Second")
                    .email("mark.second@test.com")
                    .password("testing")
                    .build();
            userRepository.save(user);
            userRepository.save(secondUser);
            //When
            List<User> users = userRepository.findAll();
            //Then
            assertNotNull(users);
            assertEquals(2, users.size());
            assertTrue(users.contains(user));
            assertTrue(users.contains(secondUser));
        }

        @Test
        void shouldFetchUserByEmail() {
            //Given
            userRepository.save(user);
            //When
            Optional<User> byEmail = userRepository.findByEmail("john.tester@test.com");
            //Then
            assertTrue(byEmail.isPresent());
            assertEquals("testing123", byEmail.get().getPassword());
        }

        @Test
        void shouldDeleteUser() {
            //Given
            userRepository.save(user);
            Long userId = user.getId();
            //When
            userRepository.delete(user);
            //Then
            Optional<User> byId = userRepository.findById(userId);
            assertNotNull(userId);
            assertFalse(byId.isPresent());
        }
    }

    @Nested
    class TasksAndWorkingDayRelationalTests {

        @Autowired
        private WorkingDayRepository workingDayRepository;

        private WorkingDay workingDay;
        private LocalDate date;

        @BeforeEach
        void setUp() {
            date = LocalDate.now();
            workingDay = WorkingDay.builder()
                    .created(date)
                    .planned(false)
                    .build();
            user.setPlannedDays(new ArrayList<>());
            user.getPlannedDays().add(workingDay);
        }

        @Test
        void shouldCreateUserWithWorkingDays() {
            //Given
            //When
            User savedUser = userRepository.save(user);
            //Then
            Long workingDayId = savedUser.getPlannedDays().get(0).getId();
            Optional<WorkingDay> workingDayById = workingDayRepository.findById(workingDayId);
            assertFalse(savedUser.getPlannedDays().isEmpty());
            assertFalse(workingDayById.isEmpty());
        }

        @Test
        void shouldDeleteOnlyUser() {
            //Given
            userRepository.save(user);
            Long workingDayId = user.getPlannedDays().get(0).getId();
            Long userId = user.getId();
            //When
            userRepository.delete(user);
            //Then
            Optional<User> userById = userRepository.findById(userId);
            assertFalse(userById.isPresent());
            assertTrue(workingDayRepository.existsById(workingDayId));
        }

        @Test
        void shouldFetchUserWithWorkingDays() {
            //Given
            userRepository.save(user);
            Long userId = user.getId();
            //When
            Optional<User> byId = userRepository.findById(userId);
            //Then
            List<WorkingDay> plannedDays = byId.get().getPlannedDays();
            assertFalse(plannedDays.isEmpty());
            assertFalse(plannedDays.get(0).isPlanned());
        }
    }
}
