package com.tensquare.base.controller;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类。
 */
@ControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理异常方法（任何一个方法报错都会调用以下方法）。
     */
    @ExceptionHandler(value = Exception.class)// 异常方法的一个标记 value ~ 定义需要捕获的异常。
    @ResponseBody// 注意：这里不能省。
    public Result error(Exception e) {
        return new Result(false, StatusCode.ERROR, e.getMessage());
    }

}
