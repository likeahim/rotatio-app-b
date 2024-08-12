package com.app.rotatio.service.strategy;

import com.app.rotatio.domain.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Getter
@SuppressWarnings("unchecked")
public class StrategyService {

    private final Map<Class<? extends ListItem>, ListStrategy<? extends ListItem>> strategyMap = new HashMap<>();

    @PostConstruct
    private void initStrategyMap() {
        strategyMap.put(Workplace.class, new WorkplaceStrategy());
        strategyMap.put(Task.class, new TaskStrategy());
        strategyMap.put(Worker.class, new WorkerStrategy());
    }

    private <T extends ListItem> ListStrategy<T> getStrategy(Class<T> itemType) {
        return (ListStrategy<T>) strategyMap.get(itemType);
    }

    public <T extends ListItem> void addItemToWorkingDay(WorkingDay workingDay, T item) {
        ListStrategy<? extends ListItem> fetchedStrategy = getStrategy(item.getClass());
        if (fetchedStrategy != null) {
            ListStrategy<T> strategy = (ListStrategy<T>) fetchedStrategy;
            strategy.addItem(workingDay, item);
        } else {
            throw new IllegalArgumentException("No strategy found for item type: " + item.getClass());
        }
    }

    public <T extends ListItem> void removeItemFromWorkingDay(WorkingDay workingDay, T item) {
        ListStrategy<? extends ListItem> fetchedStrategy = getStrategy(item.getClass());
        if (fetchedStrategy != null) {
            ListStrategy<T> strategy = (ListStrategy<T>) fetchedStrategy;
            strategy.removeItem(workingDay, item);
        } else {
            throw new IllegalArgumentException("No strategy found for item type: " + item.getClass());
        }
    }
}
