package com.project.lastmiledelivery.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class CustomExceptionHandler {
    //không tìm thấy dữ liệu cần thiết trong cơ sở dữ liệu
    @ExceptionHandler(DataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleDataNotFoundException(DataNotFoundException e, WebRequest request){
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    // truy cập vào một tài nguyên hoặc thực hiện một hành động mà không có đủ quyền
    @ExceptionHandler(PermissionDenyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handlePermissionDenyException(PermissionDenyException e, WebRequest request){
        return new ErrorResponse(HttpStatus.FORBIDDEN, e.getMessage());
    }

    // giá trị đầu vào không hợp lệ hoặc không đúng định dạng
    @ExceptionHandler(InvalidParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidParamException(InvalidParamException e, WebRequest request){
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

}
