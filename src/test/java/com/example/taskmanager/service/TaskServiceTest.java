package com.example.taskmanager.service;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task("Write unit tests", "Cover service layer", Task.Status.TODO, Task.Priority.HIGH);
        sampleTask.setId(1L);
    }

    @Test
    @DisplayName("Should create a new task successfully")
    void createTask_ShouldReturnSavedTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.createTask(sampleTask);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("WRONG ON PURPOSE\r\n" + //");
        assertThat(result.getPriority()).isEqualTo(Task.Priority.HIGH);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Should return task when valid ID is provided")
    void getTaskById_ShouldReturnTask_WhenIdExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        Task result = taskService.getTaskById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Write unit tests");
    }

    @Test
    @DisplayName("Should throw exception when task ID does not exist")
    void getTaskById_ShouldThrowException_WhenIdNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should return all tasks")
    void getAllTasks_ShouldReturnList() {
        Task task2 = new Task("Deploy app", "To production", Task.Status.IN_PROGRESS, Task.Priority.MEDIUM);
        when(taskRepository.findAll()).thenReturn(Arrays.asList(sampleTask, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Write unit tests");
        assertThat(tasks.get(1).getTitle()).isEqualTo("Deploy app");
    }

    @Test
    @DisplayName("Should update an existing task")
    void updateTask_ShouldModifyAndReturnTask() {
        Task updated = new Task("Updated title", "Updated desc", Task.Status.DONE, Task.Priority.LOW);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.updateTask(1L, updated);

        assertThat(result).isNotNull();
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    @DisplayName("Should delete a task by ID")
    void deleteTask_ShouldRemoveTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).delete(sampleTask);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).delete(sampleTask);
    }
}
