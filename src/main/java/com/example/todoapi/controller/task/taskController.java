package com.example.todoapi.controller.task;


import com.example.todoapi.controller.TasksApi;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class taskController implements TasksApi {

    @Override
    public ResponseEntity<Void> tasks1Get() {
        return ResponseEntity.ok().build();
    }
}
