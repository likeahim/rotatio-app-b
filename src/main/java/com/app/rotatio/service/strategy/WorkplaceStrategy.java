package com.app.rotatio.service.strategy;

import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.Workplace;

public class WorkplaceStrategy implements ListStrategy<Workplace> {
    @Override
    public void addItem(WorkingDay workingDay, Workplace listItem) {
        workingDay.getWorkplaces().add(listItem);
        listItem.setWorkingDay(workingDay);
    }

    @Override
    public void removeItem(WorkingDay workingDay, Workplace listItem) {
        workingDay.getWorkplaces().remove(listItem);
        listItem.setWorkingDay(null);
    }
}
