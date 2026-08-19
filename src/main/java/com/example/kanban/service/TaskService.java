package com.example.kanban.service;

import com.example.kanban.dto.TaskRequest;
import com.example.kanban.entity.Task;
import com.example.kanban.entity.TaskPriority;
import com.example.kanban.entity.TaskStatus;
import com.example.kanban.exception.TaskNotFoundException;
import com.example.kanban.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public Task create(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status() != null ? request.status() : TaskStatus.TODO);
        task.setPriority(request.priority() != null ? request.priority() : TaskPriority.MEDIUM);
        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long id, TaskRequest request) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(request.title());
                    task.setDescription(request.description());

                    if (request.status() != null)
                        task.setStatus(request.status());

                    if (request.priority() != null)
                        task.setPriority(request.priority());

                    return task;
                }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    public void delete(Long id) {
        if (!taskRepository.existsById(id))
            throw new TaskNotFoundException(id);

        taskRepository.deleteById(id);
    }
}
