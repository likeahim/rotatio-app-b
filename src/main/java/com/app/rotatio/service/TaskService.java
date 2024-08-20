package com.app.rotatio.service;

import com.app.rotatio.controller.exception.TaskAlreadyExistsException;
import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    public Task saveTask(final Task task) throws TaskAlreadyExistsException {
        List<Task> list = getAllTasks().stream()
                .filter(t -> !t.getName().equals(task.getName()))
                .toList();
        if(list.isEmpty()) {
            return taskRepository.save(task);
        } else {
            throw new TaskAlreadyExistsException();
        }
    }

    public Task delete(final Task task) {
        task.setPerformed(false);
        return taskRepository.save(task);
    }

    public Task updatePerformed(final Long id, final boolean performed) throws TaskNotFoundException {
        Task taskById = getTaskById(id);
        taskById.setPerformed(performed);
        return taskRepository.save(taskById);
    }

    public Task getTaskById(final long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksByPerformed(boolean performed) {
        return taskRepository.findByPerformed(performed);
    }
}
