package com.example.effectiveMobile_test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDTO {
    @NotBlank(message = "Content cannot be blank")
    private String content;

    @NotNull(message = "Comment author cannot be null")
    private UserDTO commentAuthor;
}
