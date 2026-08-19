package com.example.kanban.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Long id){
        super("找不到任務, id = " + id);
    }
}
