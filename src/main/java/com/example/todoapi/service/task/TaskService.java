package com.example.todoapi.service.task;

import com.example.todoapi.repository.task.TaskRecord;
import com.example.todoapi.repository.task.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;


    public List<TaskEntity> find(int limit, long offset) {
        return taskRepository.selectList(limit, offset)
               .stream()
               .map(record -> new TaskEntity(record.getId(),record.getTitle()))
               .collect(Collectors.toList());
    }


    public TaskEntity find(Long taskId) {
        return taskRepository.select(taskId)
                .map(record -> new TaskEntity(record.getId(), record.getTitle()))
                .orElseThrow(() -> new TaskEntityNotFoundException(taskId));
    }


    public TaskEntity create(String title) {
        var record = new TaskRecord(null, title);
        taskRepository.insert(record);

        return new TaskEntity(record.getId(), record.getTitle());
    }

    public TaskEntity update(Long taskId, String title) {
        /*この↓2行がないとリソースがない時にreturn文のところまで行ってfind()の中でExceptionがスローされる。
        ➡ update()の後に例外が投げられてしまう。リソースがない時にupdate()が実行しようとされるのはおかしいので
        （実際には実行できないので文が発行されるだけ）
        先にselect()でそのidにリソースがあるか確認してしまう。
        * */
        taskRepository.select(taskId)
                        .orElseThrow(() -> new TaskEntityNotFoundException(taskId));
        taskRepository.update(new TaskRecord(taskId, title));
        return find(taskId);
    }

    public void delete(Long taskId) {
//        taskRepository.find(taskId)
//                        .orElse
        taskRepository.delete(taskId);
    }
}
