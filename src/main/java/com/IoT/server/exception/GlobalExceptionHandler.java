package com.IoT.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Xử lý lỗi 400: Tham số không hợp lệ hoặc thiếu tham số
    // Xử lý lỗi kiểu dữ liệu không khớp
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleJsonParseError(HttpMessageNotReadableException ex) {
        String error = "Dữ liệu JSON không hợp lệ. Vui lòng kiểm tra lại định dạng và kiểu dữ liệu.";
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("Thiếu tham số bắt buộc: %s", ex.getParameterName());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    // Xử lý các lỗi IllegalArgumentException (lỗi tham số không hợp lệ khác)
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Xử lý lỗi 500: Bất kỳ lỗi server nào (ví dụ: MqttException, NullPointerException)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // Ghi log lỗi (nếu cần)
        return new ResponseEntity<>("Đã xảy ra lỗi trên server: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
