package com.example.todoapi.controller.task;


import com.example.todoapi.controller.TasksApi;
import com.example.todoapi.model.PageDTO;
import com.example.todoapi.model.TaskDTO;
import com.example.todoapi.model.TaskForm;
import com.example.todoapi.model.TaskListDTO;
import com.example.todoapi.service.task.TaskEntity;
import com.example.todoapi.service.task.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * タスク管理APIのコントローラークラスです。
 * <p>
 * このクラスは{@link TasksApi｝ インターフェースを実装し、
 * タスクの取得や作成を担当するエンドポイントを提供します。
 * </p>
 */
@RestController
@RequiredArgsConstructor
public class taskController implements TasksApi {

    private final TaskService taskService;

    /**
     * 指定されたタスクIDに基づいてタスクの詳細を取得するエンドポイント。
     *
     * @param taskId 詳細を取得するタスクのID (required)
     * @return タスクの詳細を含む {@link TaskDTO}を格納した{@link ResponseEntity}
     */
    @Override
    public ResponseEntity<TaskDTO> showTask(Long taskId) {
        var entity = taskService.find(taskId);

        var dto = toTaskDTO(entity);
        return ResponseEntity.ok(dto);
    }


    /**
     * 新しいタスクを作成するエンドポイント。
     * <p>
     * リクエストから渡された{@link TaskForm}に基づきタスクを作成し、
     * 作成されたタスクの情報をレスポンスとして返します。
     * </p>
     *
     * @param form 作成するタスクのデータを含む{@link TaskForm}
     * @return 作成されたタスクの詳細を含む {@link TaskDTO}を格納した{@link ResponseEntity}
     */
    @Override
    public ResponseEntity<TaskDTO> createTask(TaskForm form) {
        // taskService.create()でタイトルをformに含まれるタイトルを持ったエンティティを作成
        var entity = taskService.create(form.getTitle());
        var dto = toTaskDTO(entity);
        //ResponseBodyにdtoを入れて返す。
        return ResponseEntity
                //created()を使うと引数に渡したものがlocationに出てくる
                //↓のように書くと「/tasks/1」のようなURIが生成される
                .created(URI.create("/tasks/" + dto.getId()))
                .body(dto);
    }

    @Override
    public ResponseEntity<TaskListDTO> listTasks(Integer limit, Long offset) {
        var entityList = taskService.find(limit, offset);
        var dtoList = entityList.stream()
                .map(taskController::toTaskDTO)
                .collect(Collectors.toList());

        var pageDTO = new PageDTO();
        pageDTO.setLimit(limit);
        pageDTO.setOffset(offset);
        pageDTO.setSize(dtoList.size());

        var dto = new TaskListDTO();
        dto.setPage(pageDTO);
        dto.setResults(dtoList);

        return ResponseEntity.ok(dto);
    }


    @Override
    public ResponseEntity<TaskDTO> editTask(Long taskId, TaskForm taskForm) {
        var taskEntity = taskService.update(taskId,taskForm.getTitle());
        var dto = toTaskDTO(taskEntity);

        return ResponseEntity.ok(dto);
    }

    private static TaskDTO toTaskDTO(TaskEntity taskEntity) {
        var taskDTO = new TaskDTO();
        taskDTO.setId(taskEntity.getId());
        taskDTO.setTitle(taskEntity.getTitle());
        return taskDTO;
    }
}
