package com.wimonrat.pet.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationObject {
    private String field;
    private String error;

}
