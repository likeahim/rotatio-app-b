package com.app.rotatio.service.strategy;

import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.WorkingDay;

public class TaskStrategy implements ListStrategy<Task> {
    @Override
    public void addItem(WorkingDay workingDay, Task listItem) {
        workingDay.getTasks().add(listItem);
        listItem.setWorkingDay(workingDay);
    }

    @Override
    public void removeItem(WorkingDay workingDay, Task listItem) {
        workingDay.getTasks().remove(listItem);
        listItem.setWorkingDay(null);
    }
}
