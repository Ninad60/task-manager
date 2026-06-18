package com.example.taskmanager.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class TaskModelTest {

    @Test
    void testDefaultConstructorAndSetters() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test");
        task.setDescription("Desc");
        task.setStatus(Task.Status.IN_PROGRESS);
        task.setPriority(Task.Priority.LOW);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        assertThat(task.getId()).isEqualTo(1L);
        assertThat(task.getTitle()).isEqualTo("Test");
        assertThat(task.getDescription()).isEqualTo("Desc");
        assertThat(task.getStatus()).isEqualTo(Task.Status.IN_PROGRESS);
        assertThat(task.getPriority()).isEqualTo(Task.Priority.LOW);
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getUpdatedAt()).isNotNull();
    }

    @Test
    void testParameterisedConstructor() {
        Task task = new Task("Title", "Desc", Task.Status.DONE, Task.Priority.HIGH);
        assertThat(task.getTitle()).isEqualTo("Title");
        assertThat(task.getStatus()).isEqualTo(Task.Status.DONE);
        assertThat(task.getPriority()).isEqualTo(Task.Priority.HIGH);
    }

    @Test
    void testOnCreateSetsTimestamps() {
        Task task = new Task();
        task.onCreate();
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getUpdatedAt()).isNotNull();
    }

    @Test
    void testOnUpdateSetsTimestamp() {
        Task task = new Task();
        task.onCreate();
        LocalDateTime before = task.getUpdatedAt();
        task.onUpdate();
        assertThat(task.getUpdatedAt()).isAfterOrEqualTo(before);
    }

    @Test
    void testAllEnumValues() {
        assertThat(Task.Status.values()).containsExactly(
            Task.Status.TODO, Task.Status.IN_PROGRESS, Task.Status.DONE);
        assertThat(Task.Priority.values()).containsExactly(
            Task.Priority.LOW, Task.Priority.MEDIUM, Task.Priority.HIGH);
    }
}