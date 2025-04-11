package com.bivgroup.exception;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HandledServiceException extends Exception {

    private Long code;

    public HandledServiceException() {
        super();
    }

    public HandledServiceException(Long code, String message) {
        super(message);
        this.code = code;
    }
}
