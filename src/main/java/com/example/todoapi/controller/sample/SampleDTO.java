package com.example.todoapi.controller.sample;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class SampleDTO {

    String content;
    LocalDateTime timestamp;

}
