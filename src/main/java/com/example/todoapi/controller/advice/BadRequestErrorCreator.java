package com.example.todoapi.controller.advice;

import com.example.todoapi.model.BadRequestError;
import com.example.todoapi.model.InvalidParam;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;

/**
 * エラーレスポンスを生成するためのユーティリティークラス
 */
public class BadRequestErrorCreator {
    /**
     * {@link MethodArgumentNotValidException}を解析し、
     * バリデーションエラーを表す{@link BadRequestError} オブジェクトを生成する。
     *
     * @param ex バリデーションエラーが発生した際にスローされる例外オブジェクト
     * @return バリデーションエラー情報を格納した {@link BadRequestError}
     */
    public static Object from(MethodArgumentNotValidException ex) {
        //getFieldError()ではなく
        var invalidParamList = ex.getFieldErrors()
                .stream()
                .map(BadRequestErrorCreator::apply)
                .collect(Collectors.toList());

        var error = new BadRequestError();
        error.setInvalidParams(invalidParamList);
        return error;
    }

    private static InvalidParam apply(FieldError fieldError) {
        var invalidParam = new InvalidParam();
        invalidParam.setName(fieldError.getField());
        invalidParam.setReason(fieldError.getDefaultMessage());
        return invalidParam;
    }
}
