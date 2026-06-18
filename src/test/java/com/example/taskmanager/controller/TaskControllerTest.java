package com.example.taskmanager.controller;

import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
//kugvkhgvgkiyvcyjgjygff
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/tasks should create a task and return 201")
    void createTask_ShouldReturn201() throws Exception {
        Task task = new Task("New task", "Description", Task.Status.TODO, Task.Priority.HIGH);
        task.setId(1L);

        when(taskService.createTask(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New task")))
                .andExpect(jsonPath("$.status", is("TODO")));
    }

    @Test
    @DisplayName("GET /api/tasks should return a list of tasks")
    void getAllTasks_ShouldReturnList() throws Exception {
        Task t1 = new Task("Task 1", "Desc 1", Task.Status.TODO, Task.Priority.LOW);
        Task t2 = new Task("Task 2", "Desc 2", Task.Status.DONE, Task.Priority.HIGH);
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(t1, t2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Task 1")));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} should return 404 when not found")
    void getTaskById_ShouldReturn404_WhenNotFound() throws Exception {
        when(taskService.getTaskById(99L)).thenThrow(new TaskNotFoundException(99L));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/tasks with blank title should return 400")
    void createTask_ShouldReturn400_WhenTitleBlank() throws Exception {
        Task invalid = new Task("", "No title", Task.Status.TODO, Task.Priority.LOW);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
