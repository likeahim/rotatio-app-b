package com.app.rotatio.service.strategy;

import com.app.rotatio.domain.ListItem;
import com.app.rotatio.domain.WorkingDay;
import org.springframework.stereotype.Component;

@Component
public interface ListStrategy <T extends ListItem> {
    void addItem(WorkingDay workingDay, T listItem);
    void removeItem(WorkingDay workingDay, T listItem);
}
