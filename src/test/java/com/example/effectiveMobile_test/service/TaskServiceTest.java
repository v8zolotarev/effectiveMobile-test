package com.example.effectiveMobile_test.service;

import com.example.effectiveMobile_test.dto.TaskDTO;
import com.example.effectiveMobile_test.dto.UserDTO;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testGetTaskById_TaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));
    }

    @Test
    void testCreateTask_Admin() {
        TaskDTO taskDTO = new TaskDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.ADMIN);
        User author = new User();
        author.setRole(Role.ADMIN);

        when(userMapper.userDTOToUser(userDTO)).thenReturn(author);

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        task.setAuthor(author);

        when(taskMapper.taskDTOToTask(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDTO(task)).thenReturn(taskDTO);

        TaskDTO createdTask = taskService.createTask(taskDTO, userDTO);
        assertNotNull(createdTask);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testCreateTask_NotAdmin() {
        TaskDTO taskDTO = new TaskDTO();
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);
        User author = new User();
        author.setRole(Role.USER);

        when(userMapper.userDTOToUser(userDTO)).thenReturn(author);

        assertThrows(UnauthorizedAccessException.class, () -> taskService.createTask(taskDTO, userDTO));
    }

    @Test
    void testUpdateTask_UserRole() {
        Long taskId = 1L;
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Updated Title");
        taskDTO.setDescription("Updated Description");
        taskDTO.setPriority(Priority.HIGH);
        taskDTO.setStatus(Status.IN_PROGRESS);

        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);
        User author = new User();
        author.setRole(Role.USER);

        Task task = new Task();
        task.setId(taskId);
        task.setAssignee(author);
        task.setTitle("Original Title");

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));
        when(userMapper.userDTOToUser(userDTO)).thenReturn(author);
        when(taskMapper.taskDTOToTask(taskDTO)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.taskToTaskDTO(task)).thenReturn(taskDTO);

        TaskDTO updatedTask = taskService.updateTask(taskId, taskDTO, userDTO);
        assertNotNull(updatedTask);
        assertEquals("Updated Title", updatedTask.getTitle());
    }

    @Test
    void testDeleteTask_Admin() {
        Long taskId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.ADMIN);
        User author = new User();
        author.setRole(Role.ADMIN);

        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));
        when(userMapper.userDTOToUser(userDTO)).thenReturn(author);

        taskService.deleteTask(taskId, userDTO);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void testDeleteTask_NotAdmin() {
        Long taskId = 1L;
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(Role.USER);
        User author = new User();
        author.setRole(Role.USER);

        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));
        when(userMapper.userDTOToUser(userDTO)).thenReturn(author);

        assertThrows(UnauthorizedAccessException.class, () -> taskService.deleteTask(taskId, userDTO));
    }
}
