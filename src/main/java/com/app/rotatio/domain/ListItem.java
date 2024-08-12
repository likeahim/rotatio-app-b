package com.app.rotatio.domain;

import lombok.Data;

import java.util.Objects;

@Data
public abstract class ListItem {

    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListItem listItem = (ListItem) o;
        return Objects.equals(id, listItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
