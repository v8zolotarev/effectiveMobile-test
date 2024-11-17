package com.example.effectiveMobile_test.service;

import com.example.effectiveMobile_test.dto.TaskDTO;
import com.example.effectiveMobile_test.dto.TaskWithCommentsDTO;
import com.example.effectiveMobile_test.dto.UserDTO;
import com.example.effectiveMobile_test.entity.Comment;
import com.example.effectiveMobile_test.entity.Task;
import com.example.effectiveMobile_test.entity.User;
import com.example.effectiveMobile_test.enums.Priority;
import com.example.effectiveMobile_test.enums.Role;
import com.example.effectiveMobile_test.enums.Status;
import com.example.effectiveMobile_test.exception.TaskNotFoundException;
import com.example.effectiveMobile_test.exception.UnauthorizedAccessException;
import com.example.effectiveMobile_test.mappers.TaskMapper;
import com.example.effectiveMobile_test.mappers.UserMapper;
import com.example.effectiveMobile_test.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserMapper userMapper;

    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
        return taskMapper.taskToTaskDTO(task);
    }

    public List<TaskWithCommentsDTO> getTasksWithComments(Long authorId, Long assigneeId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Task> tasks;

        if (authorId != null && assigneeId != null) {
            tasks = taskRepository.findByAuthorIdAndAssigneeId(authorId, assigneeId, pageable);
        } else if (authorId != null) {
            tasks = taskRepository.findByAuthorId(authorId, pageable);
        } else if (assigneeId != null) {
            tasks = taskRepository.findByAssigneeId(assigneeId, pageable);
        } else {
            tasks = taskRepository.findAll(pageable);
        }

        return tasks.getContent().stream().map(task -> {
            TaskWithCommentsDTO dto = new TaskWithCommentsDTO();
            dto.setTask(taskMapper.taskToTaskDTO(task));
            dto.setComments(task.getComments().stream().map(Comment::getContent).toList());
            return dto;
        }).toList();
    }

    public TaskDTO createTask(TaskDTO taskDTO, UserDTO userDTO) {
        User author = userMapper.userDTOToUser(userDTO);

        if (author.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only admins can create tasks");
        }

        Task task = taskMapper.taskDTOToTask(taskDTO);
        task.setAuthor(author);
        Task savedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(savedTask);
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO, UserDTO userDTO) {
        User author = userMapper.userDTOToUser(userDTO);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (author.getRole() == Role.USER && !task.getAssignee().equals(author)) {
            throw new UnauthorizedAccessException("You can only edit tasks assigned to you");
        }

        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        task.setAssignee(userMapper.userDTOToUser(taskDTO.getAssignee()));
        task.setAuthor(userMapper.userDTOToUser(taskDTO.getAuthor()));

        Task updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(updatedTask);
    }

    public void deleteTask(Long taskId, UserDTO userDTO) {
        User author = userMapper.userDTOToUser(userDTO);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (author.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only admins can delete tasks");
        }

        taskRepository.deleteById(taskId);
    }

    public TaskDTO changeTaskStatus(Long taskId, Status status, UserDTO userDTO) {
        User author = userMapper.userDTOToUser(userDTO);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (author.getRole() == Role.USER && !task.getAssignee().equals(author)) {
            throw new UnauthorizedAccessException("You can only change the status of tasks assigned to you");
        }

        task.setStatus(status);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(updatedTask);
    }

    public TaskDTO changeTaskPriority(Long taskId, Priority priority, UserDTO userDTO) {
        User author = userMapper.userDTOToUser(userDTO);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (author.getRole() == Role.USER && !task.getAssignee().equals(author)) {
            throw new UnauthorizedAccessException("You can only change the priority of tasks assigned to you");
        }

        task.setPriority(priority);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(updatedTask);
    }

    public TaskDTO assignTask(Long taskId, UserDTO assigneeDTO, UserDTO authorDTO) {
        User assignee = userMapper.userDTOToUser(assigneeDTO);
        User author = userMapper.userDTOToUser(authorDTO);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        if (author.getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("Only admins can assign tasks");
        }

        task.setAssignee(assignee);
        Task updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(updatedTask);
    }

    public TaskDTO addCommentToTask(Long taskId, String content, UserDTO commentAuthorDTO) {
        User commentAuthor = userMapper.userDTOToUser(commentAuthorDTO);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setCommentAuthor(commentAuthor);
        task.getComments().add(comment);

        Task updatedTask = taskRepository.save(task);
        return taskMapper.taskToTaskDTO(updatedTask);
    }
}
