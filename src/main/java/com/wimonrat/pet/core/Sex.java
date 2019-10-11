package com.wimonrat.pet.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.wimonrat.pet.exception.InvalidEnumValueException;

import java.util.Arrays;
import java.util.Optional;

import static com.wimonrat.pet.errorcode.ResponseCode.INVALID_SEX_TYPE;

public enum Sex {
    F, M;

    @JsonCreator
    public static Sex getSex(String val) throws InvalidEnumValueException {
        if (val == null) {
            return null;
        }
        Optional<Sex> sex = Arrays.stream(Sex.values())
                .parallel()
                .filter(ct -> ct.toString().equalsIgnoreCase(val))
                .findAny();
        return sex.orElseThrow(() -> new InvalidEnumValueException(INVALID_SEX_TYPE, INVALID_SEX_TYPE.getMessage()));
    }
}
