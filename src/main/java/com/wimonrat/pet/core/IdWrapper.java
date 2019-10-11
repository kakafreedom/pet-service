package com.wimonrat.pet.core;

import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Set;

@Getter
public class IdWrapper {
    @NotEmpty
    Set<Long> ids;
}
