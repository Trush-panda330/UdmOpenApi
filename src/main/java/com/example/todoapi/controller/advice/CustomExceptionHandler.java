package com.example.todoapi.controller.advice;

import com.example.todoapi.model.BadRequestError;
import com.example.todoapi.model.ResourceNotFoundError;
import com.example.todoapi.service.task.TaskEntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * カスタム例外を処理し、適切なエラーレスポンスを返すための例外ハンドラークラス。
 * <p>
 *     このクラスは {@link RestControllerAdvice} を利用してアプリケーションz船体の例外処理を統一
 * </p>
 */
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * {@link TaskEntityNotFoundException} を処理し、
     * 404 Not Found ステータスのエラーレスポンスを返します。
     *
     * @param e 対象のエンティティが見つからなかった時にスローされる例外
     * @return {@link ResourceNotFoundError} を含むレスポンスエンティティ
     */
    @ExceptionHandler(TaskEntityNotFoundException.class)
    public ResponseEntity<ResourceNotFoundError> handleTaskEntityNotFoundException(TaskEntityNotFoundException e) {
        var error = new ResourceNotFoundError();
        error.setDetail(e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    /**
     * バリデーションエラーを処理し、400 Bad Request ステータスのエラーレスポンスを返します。
     *
     * @param ex バリデーションエラーが発生した際にスローされる例外
     * @param headers レスポンスヘッダー
     * @param status Httpステータス
     * @param request リクエスト情報
     * @return {@link BadRequestError} を含むレスポンスエンティティ
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        var error = BadRequestErrorCreator.from(ex);
        return ResponseEntity.badRequest().body(error);
    }
}
