package com.app.rotatio.domain.dto;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public record TaskDto(
        Long id,
        @NotNull
        String name,
        String description,
        boolean isPerformed
) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDto taskDto = (TaskDto) o;
        return isPerformed == taskDto.isPerformed && Objects.equals(id, taskDto.id) && Objects.equals(name, taskDto.name) && Objects.equals(description, taskDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isPerformed);
    }
}
