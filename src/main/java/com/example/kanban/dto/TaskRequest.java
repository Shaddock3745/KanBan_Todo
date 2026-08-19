package com.example.kanban.dto;

import com.example.kanban.entity.TaskPriority;
import com.example.kanban.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;

public record TaskRequest(@NotBlank(message = "title 不可為空") String title,
                          String description, TaskStatus status, TaskPriority priority) {}
