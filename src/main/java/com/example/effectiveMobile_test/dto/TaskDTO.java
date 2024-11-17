package com.example.effectiveMobile_test.dto;

import com.example.effectiveMobile_test.enums.Priority;
import com.example.effectiveMobile_test.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TaskDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Status cannot be null")
    private Status status;

    @NotNull(message = "Priority cannot be null")
    private Priority priority;

    @NotNull(message = "Author cannot be null")
    private UserDTO author;

    private UserDTO assignee;

    private List<CommentDTO> comments;
}
