package com.felfel.hogwarts_artifacts_online.system.exception;

import com.felfel.hogwarts_artifacts_online.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvise {

    @ExceptionHandler(OpjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(OpjectNotFoundException ex)
    {
        return new Result(false, HttpStatus.NOT_FOUND.value(),ex.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex)
    {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String,String> map = new HashMap<>(errors.size());
        errors.forEach((error)->{
            String key = ((FieldError) error).getField();
            String value = error.getDefaultMessage();
            map.put(key,value);
        });

        return new Result(false,
                                HttpStatus.BAD_REQUEST.value(),
                        "Provided arguments are invalid",
                                map);
    }

}
