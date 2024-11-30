package com.example.todoapi.controller.advice;

import com.example.todoapi.model.BadRequestError;
import com.example.todoapi.model.InvalidParam;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ElementKind;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public static BadRequestError from(ConstraintViolationException ex) {
        var invalidParamList = ex.getConstraintViolations()
                .stream()
                .map(BadRequestErrorCreator::createInvalidParam)
                .collect(Collectors.toList());

        var error = new BadRequestError();
        error.setInvalidParams(invalidParamList);
        return error;
    }

    private static InvalidParam createInvalidParam(ConstraintViolation<?> violation) {
        var parameterOpt = StreamSupport.stream(violation.getPropertyPath().spliterator(),false)
                .filter(node -> node.getKind().equals(ElementKind.PARAMETER))
                .findFirst();

        var invalidParam = new InvalidParam();
        parameterOpt.ifPresent(p -> invalidParam.setName(p.getName()));
        invalidParam.setReason(violation.getMessage());

        return invalidParam;
    }
}
