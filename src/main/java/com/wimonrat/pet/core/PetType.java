package com.wimonrat.pet.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.wimonrat.pet.errorcode.ResponseCode;
import com.wimonrat.pet.exception.InvalidEnumValueException;

import java.util.Arrays;
import java.util.Optional;

import static com.wimonrat.pet.errorcode.ResponseCode.INVALID_PET_TYPE;

public enum PetType {
    CAT, DOG, BIRD, FISH;

    @JsonCreator
    public static PetType getType(String key) throws InvalidEnumValueException {
        if (key == null) {
            return null;
        }
        Optional<PetType> petType = Arrays.stream(PetType.values())
                .parallel()
                .filter(ct -> ct.toString().equalsIgnoreCase(key))
                .findAny();
        return petType.orElseThrow(() -> new InvalidEnumValueException(INVALID_PET_TYPE, INVALID_PET_TYPE.getMessage()));
    }
}
