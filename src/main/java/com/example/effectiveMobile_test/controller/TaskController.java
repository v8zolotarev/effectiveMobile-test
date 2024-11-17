package com.example.effectiveMobile_test.controller;

import com.example.effectiveMobile_test.dto.TaskDTO;
import com.example.effectiveMobile_test.dto.TaskWithCommentsDTO;
import com.example.effectiveMobile_test.dto.UserDTO;
import com.example.effectiveMobile_test.enums.Priority;
import com.example.effectiveMobile_test.enums.Status;
import com.example.effectiveMobile_test.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @GetMapping
    public ResponseEntity<List<TaskWithCommentsDTO>> getTasksWithComments(
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(taskService.getTasksWithComments(authorId, assigneeId, page, size));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO, @RequestParam UserDTO author) {
        return ResponseEntity.ok(taskService.createTask(taskDTO, author));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long taskId,
                                              @RequestBody TaskDTO taskDTO,
                                              @RequestParam UserDTO author) {
        return ResponseEntity.ok(taskService.updateTask(taskId, taskDTO, author));
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId, @RequestParam UserDTO author) {
        taskService.deleteTask(taskId, author);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> changeTaskStatus(@PathVariable Long taskId,
                                                    @RequestParam Status status,
                                                    @RequestParam UserDTO author) {
        return ResponseEntity.ok(taskService.changeTaskStatus(taskId, status, author));
    }

    @PatchMapping("/{taskId}/priority")
    public ResponseEntity<TaskDTO> changeTaskPriority(@PathVariable Long taskId,
                                                      @RequestParam Priority priority,
                                                      @RequestParam UserDTO author) {
        return ResponseEntity.ok(taskService.changeTaskPriority(taskId, priority, author));
    }

    @PatchMapping("/{taskId}/assignee")
    public ResponseEntity<TaskDTO> assignTask(@PathVariable Long taskId,
                                              @RequestBody UserDTO assignee,
                                              @RequestParam UserDTO author) {
        return ResponseEntity.ok(taskService.assignTask(taskId, assignee, author));
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<TaskDTO> addCommentToTask(@PathVariable Long taskId,
                                                    @RequestParam String content,
                                                    @RequestBody UserDTO commentAuthor) {
        return ResponseEntity.ok(taskService.addCommentToTask(taskId, content, commentAuthor));
    }
}