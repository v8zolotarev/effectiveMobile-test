package com.example.effectiveMobile_test.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskWithCommentsDTO {
    private TaskDTO task;
    private List<String> comments;
}
