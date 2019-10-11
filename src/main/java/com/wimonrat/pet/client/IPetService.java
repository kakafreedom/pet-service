package com.wimonrat.pet.client;

import com.wimonrat.pet.core.CreatePet;
import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.core.PetType;
import com.wimonrat.pet.core.Sex;
import com.wimonrat.pet.exception.MissingFieldException;
import com.wimonrat.pet.exception.PetNotFoundException;

import java.util.List;
import java.util.Set;

public interface IPetService {

    List<Pet> search(Long id, PetType type, Integer age, String ownerEmail, Sex sex);

    Set<Pet> create(Set<CreatePet> pets);

    Set<Pet> update(Set<Pet> pets) throws PetNotFoundException, MissingFieldException;

    void delete(Set<Long> ids) throws PetNotFoundException;
}
