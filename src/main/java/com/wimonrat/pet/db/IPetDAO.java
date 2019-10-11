package com.wimonrat.pet.db;

import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.core.PetType;
import com.wimonrat.pet.core.Sex;
import com.wimonrat.pet.exception.PetNotFoundException;

import java.util.List;

public interface IPetDAO {

    Pet create(Pet pet);
    Pet update(Pet pet);
    void delete(Pet pet);
    Pet findById(Long id) throws PetNotFoundException;
    List<Pet> search(Long id, PetType type, Integer age, String ownerEmail, Sex sex);
}
