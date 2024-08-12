package com.app.rotatio.service.strategy;

import com.app.rotatio.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StrategyServiceTests {

    @Autowired
    private StrategyService strategyService;

    @Test
    void shouldFetchMap() {
        //Given
        //When
        Map<Class<? extends ListItem>, ListStrategy<? extends ListItem>> strategyMap = strategyService.getStrategyMap();
        //Then
        assertNotNull(strategyMap);
        assertEquals(3, strategyMap.size());
    }
}