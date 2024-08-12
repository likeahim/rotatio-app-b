package com.app.rotatio.service.strategy;

import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.WorkingDay;

public class WorkerStrategy implements ListStrategy<Worker> {
    @Override
    public void addItem(WorkingDay workingDay, Worker listItem) {
        workingDay.getWorkers().add(listItem);
        listItem.setWorkingDay(workingDay);
    }

    @Override
    public void removeItem(WorkingDay workingDay, Worker listItem) {
        workingDay.getWorkers().remove(listItem);
        listItem.setWorkingDay(null);
    }
}
