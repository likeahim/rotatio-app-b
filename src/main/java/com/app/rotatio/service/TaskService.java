package com.app.rotatio.service;

import com.app.rotatio.controller.exception.TaskAlreadyExistsException;
import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.repository.TaskRepository;
import com.app.rotatio.repository.WorkingDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final WorkingDayService workingDayService;

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

    public Task getTaskById(final long id) throws TaskNotFoundException {
        return taskRepository.findById(id).orElseThrow(TaskNotFoundException::new);
    }

    public Task getTaskByName(final String name) throws TaskNotFoundException {
        return taskRepository.findByName(name).orElseThrow(TaskNotFoundException::new);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public List<Task> getAllTasksByWorkingDay(final Long id) throws WorkingDayNotFoundException {
        WorkingDay byId = workingDayService.getWorkingDayById(id);
        return taskRepository.findAllByWorkingDay(byId);
    }
}
