package com.example.effectiveMobile_test.mappers;

import com.example.effectiveMobile_test.dto.TaskDTO;
import com.example.effectiveMobile_test.entity.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDTO taskToTaskDTO(Task task);

    Task taskDTOToTask(TaskDTO taskDTO);
}
