package com.felfel.hogwarts_artifacts_online.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Result.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private Boolean flag;
    private Integer code;
    private String message;
    private Object data;

    /**
     * Instantiates a new Result.
     *
     * @param flag    the flag
     * @param code    the code
     * @param message the message
     */
    public Result(Boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }
}
