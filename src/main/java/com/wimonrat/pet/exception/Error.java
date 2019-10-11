package com.wimonrat.pet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Error {
    private final String errorCode;
    private final String message;
}
