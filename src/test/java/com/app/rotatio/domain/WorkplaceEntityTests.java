package com.app.rotatio.domain;

import com.app.rotatio.repository.WorkplaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkplaceEntityTests {

    @Autowired
    private WorkplaceRepository workplaceRepository;

    private Workplace workplace;

    @BeforeEach
    void setUp() {
        workplace = Workplace.builder()
                .designation("Test")
                .build();
    }

    @Nested
    class WorkplaceRepositorySimpleTests {

        @Test
        void shouldCreateWorkplace() {
            //Given
            Long idBeforeSave = workplace.getId();
            //When
            workplaceRepository.save(workplace);
            //Then
            assertNotEquals(idBeforeSave, workplace.getId());
            assertTrue(workplaceRepository.findById(workplace.getId()).isPresent());
        }

        @Test
        void shouldUpdateWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            String designationBefore = workplace.getDesignation();
            boolean activeBefore = workplace.isActive();
            boolean nowUsedBefore = workplace.isNowUsed();
            workplace.setDesignation("Updated");
            workplace.setActive(true);
            workplace.setNowUsed(true);
            //When
            workplaceRepository.save(workplace);
            //Then
            assertNotEquals(designationBefore, workplace.getDesignation());
            assertNotEquals(activeBefore, workplace.isActive());
            assertNotEquals(nowUsedBefore, workplace.isNowUsed());
        }

        @Test
        void shouldDeleteWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            Long workplaceId = workplace.getId();
            //When
            workplaceRepository.delete(workplace);
            //Then
            assertNotNull(workplaceId);
            assertFalse(workplaceRepository.findById(workplaceId).isPresent());
        }
    }

    @Nested
    class WorkplaceFetchingTests {

        private Workplace secondWorkplace;

        @BeforeEach
        void setUp() {
            secondWorkplace = Workplace.builder()
                    .nowUsed(true)
                    .active(true)
                    .designation("Second workplace")
                    .build();
        }

        @Test
        void shouldFetchWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            Long workplaceId = workplace.getId();
            //When
            Optional<Workplace> foundById = workplaceRepository.findById(workplaceId);
            //Then
            assertTrue(foundById.isPresent());
            assertEquals("Test", foundById.get().getDesignation());
        }

        @Test
        void shouldFetchAllWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            workplaceRepository.save(secondWorkplace);
            Long workplaceId = workplace.getId();
            Long secondWorkplaceId = secondWorkplace.getId();
            //When
            List<Workplace> allWorkplaces = workplaceRepository.findAll();
            //Then
            long count = allWorkplaces.stream()
                    .filter(w -> w.getId().equals(workplaceId))
                    .count();
            assertEquals(2, allWorkplaces.size());
            assertTrue(allWorkplaces.contains(workplace));
            assertTrue(allWorkplaces.contains(secondWorkplace));
            assertEquals(1, count);
        }

        @Test
        void shouldFetchActiveWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> activeWorkplaces = workplaceRepository.findByActive(true);
            //Then
            assertEquals(1, activeWorkplaces.size());
            assertFalse(activeWorkplaces.contains(workplace));
        }

        @Test
        void shouldFetchNotActiveWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            secondWorkplace.setActive(false);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> notActiveWorkplaces = workplaceRepository.findByActive(false);
            //Then
            assertEquals(2, notActiveWorkplaces.size());
            assertTrue(notActiveWorkplaces.contains(workplace));
            assertTrue(notActiveWorkplaces.contains(secondWorkplace));
        }

        @Test
        void shouldFetchNowUsedWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            secondWorkplace.setNowUsed(true);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> nowUsedWorkplaces = workplaceRepository.findByNowUsed(true);
            //Then
            assertEquals(1, nowUsedWorkplaces.size());
            assertFalse(nowUsedWorkplaces.contains(workplace));
            assertTrue(nowUsedWorkplaces.contains(secondWorkplace));
        }

        @Test
        void shouldFetchNotUsedWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            secondWorkplace.setNowUsed(true);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> notUsedWorkplaces = workplaceRepository.findByNowUsed(false);
            //Then
            assertEquals(1, notUsedWorkplaces.size());
            assertTrue(notUsedWorkplaces.contains(workplace));
            assertFalse(notUsedWorkplaces.contains(secondWorkplace));
        }
    }
}