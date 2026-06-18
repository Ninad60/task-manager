package com.example.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskManagerApplicationTest {

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodRuns() {
        TaskManagerApplication.main(new String[]{});
    }
}