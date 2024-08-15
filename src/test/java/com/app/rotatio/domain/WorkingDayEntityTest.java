package com.app.rotatio.domain;

import com.app.rotatio.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkingDayEntityTest {

    @Autowired
    private WorkingDayRepository workingDayRepository;
    @Autowired
    private UserRepository userRepository;

    private WorkingDay workingDay;

    @BeforeEach
    void setUp() {
        workingDay = WorkingDay.builder()
                .created(LocalDate.now())
                .build();
    }

    @Nested
    class WorkingDaysRepositorySimpleTests {

        @Test
        void shouldCreateWorkingDay() {
            //Given
            Long workingDayId = workingDay.getId();
            //When
            workingDayRepository.save(workingDay);
            //Then
            assertNotNull(workingDay.getId());
            assertNotEquals(workingDayId, workingDay.getId());
            assertTrue(workingDayRepository.findById(workingDay.getId()).isPresent());
        }

        @Test
        void shouldUpdateWorkingDay() {
            //Given
            workingDayRepository.save(workingDay);
            workingDay.setCreated(LocalDate.now().plusDays(1));
            workingDay.setExecuteDate(workingDay.getCreated().plusDays(1));
            workingDay.setPlanned(true);
            //When
            workingDayRepository.save(workingDay);
            //Then
            WorkingDay foundWorkingDay = workingDayRepository.findById(workingDay.getId()).orElseThrow();
            assertEquals(LocalDate.now().plusDays(1), foundWorkingDay.getCreated());
            assertEquals(LocalDate.now().plusDays(2), foundWorkingDay.getExecuteDate());
            assertTrue(foundWorkingDay.isPlanned());
        }

        @Test
        void shouldDeleteWorkingDay() {
            //Given
            workingDayRepository.save(workingDay);
            Long workingDayId = workingDay.getId();
            //When
            workingDayRepository.delete(workingDay);
            //Then
            assertEquals(0, workingDayRepository.findAll().size());
            assertTrue(workingDayRepository.findById(workingDayId).isEmpty());
        }

        @Nested
        class WorkingDayFetchingTests {

            private WorkingDay secondWorkingDay;

            @BeforeEach
            void setUp() {
                secondWorkingDay = WorkingDay.builder()
                        .created(LocalDate.now().plusDays(1))
                        .build();
            }
            @Test
            void shouldFetchWorkingDayById() {
                //Given
                workingDayRepository.save(workingDay);
                Long workingDayId = workingDay.getId();
                //When
                Optional<WorkingDay> workingDayById = workingDayRepository.findById(workingDayId);
                //Then
                assertTrue(workingDayById.isPresent());
                assertEquals(LocalDate.now(), workingDayById.get().getCreated());
            }

            @Test
            void shouldFetchAllWorkingDays() {
                //Given
                workingDayRepository.save(workingDay);
                workingDayRepository.save(secondWorkingDay);
                //When
                List<WorkingDay> allWorkingDays = workingDayRepository.findAll();
                //Then
                assertEquals(2, allWorkingDays.size());
                assertTrue(allWorkingDays.contains(secondWorkingDay));
                assertTrue(allWorkingDays.contains(workingDay));
            }

            @Test
            void shouldFetchAllWorkingDaysByPlanned() {
                //Given
                workingDay.setPlanned(true);
                secondWorkingDay.setPlanned(false);
                workingDayRepository.save(workingDay);
                workingDayRepository.save(secondWorkingDay);
                //When
                List<WorkingDay> byPlanned = workingDayRepository.findByPlanned(true);
                //Then
                assertEquals(1, byPlanned.size());
                assertFalse(byPlanned.contains(secondWorkingDay));
                assertEquals(LocalDate.now(), byPlanned.get(0).getCreated());
            }

            @Test
            void shouldFetchAllWorkingDaysExecutingBeforeDate() {
                //Given
                LocalDate executeDate = LocalDate.now().minusDays(4);
                workingDay.setExecuteDate(executeDate);
                secondWorkingDay.setExecuteDate(executeDate);
                workingDayRepository.save(workingDay);
                workingDayRepository.save(secondWorkingDay);
                //When
                LocalDate dateToCheck = executeDate.plusDays(2);
                List<WorkingDay> byExecuteDateBefore = workingDayRepository.findByExecuteDateBefore(dateToCheck);
                //Then
                assertFalse(byExecuteDateBefore.isEmpty());
                assertEquals(2, byExecuteDateBefore.size());
                assertTrue(byExecuteDateBefore.contains(workingDay));
                assertTrue(byExecuteDateBefore.contains(secondWorkingDay));
            }
        }
        @Nested
        class WorkingDayAndUserRelationalTests {
            private User user;

            @BeforeEach
            void setUp() {
                user = User.builder()
                        .firstName("John")
                        .lastname("Tester")
                        .email("tester@test.com")
                        .password("testing123")
                        .build();
            }

            @Test
            void shouldFetchAllWorkingDaysByUser() {
                //Given
                userRepository.save(user);
                workingDay.setUser(user);
                workingDayRepository.save(workingDay);
                //When
                List<WorkingDay> byUser = workingDayRepository.findByUser(user);
                //Then
                assertEquals(1, byUser.size());
                assertTrue(byUser.contains(workingDay));
            }

            @Test
            void shouldNotCreateUserAndThrowException() {
                //Given
                workingDay.setUser(user);
                //When
                workingDayRepository.save(workingDay);
                //Then
                assertThrows(Exception.class, () -> workingDayRepository.findByUser(user));
                assertNull(user.getId());
            }

            @Test
            void shouldNotDeleteUser() {
                //Given
                userRepository.save(user);
                workingDay.setUser(user);
                workingDayRepository.save(workingDay);
                //When
                workingDayRepository.delete(workingDay);
                //Then
                assertTrue(userRepository.findById(user.getId()).isPresent());
            }
        }
    }
}