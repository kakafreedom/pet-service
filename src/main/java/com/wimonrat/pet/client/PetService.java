package com.wimonrat.pet.client;

import com.wimonrat.pet.core.CreatePet;
import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.core.PetType;
import com.wimonrat.pet.core.Sex;
import com.wimonrat.pet.db.PetDAO;
import com.wimonrat.pet.errorcode.ResponseCode;
import com.wimonrat.pet.exception.MissingFieldException;
import com.wimonrat.pet.exception.PetNotFoundException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PetService implements IPetService {

    private final PetDAO dao;

    public PetService(PetDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<Pet> search(Long id, PetType type, Integer age, String ownerEmail, Sex sex) {
        return dao.search(id, type, age, ownerEmail, sex);
    }

    @Override
    public Set<Pet> create(Set<CreatePet> pets) {

        Set<Pet> createdPets = new HashSet<>();
        pets.stream().forEach(pet -> {
            createdPets.add(dao.create(CreatePet.toPet(pet)));
        });
        return createdPets;
    }

    @Override
    public Set<Pet> update(Set<Pet> pets) throws PetNotFoundException, MissingFieldException {
        Set<Pet> updatedPets = new HashSet<>();
        preValidation(pets);
        for (Pet pet : pets) {
            updatedPets.add(update(pet));
        }
        return updatedPets;
    }

    @Override
    public void delete(Set<Long> ids) throws PetNotFoundException {
        for (long id : ids) {
            dao.delete(dao.findById(id));
        }
    }

    private void preValidation(Set<Pet> pets) throws MissingFieldException {
        for (Pet pet : pets) {
            if (pet.getId() == null) {
                throw new MissingFieldException(ResponseCode.MISSING_REQUIRED_FIELDS, "Id is required.");
            }
        }
    }

    private Pet update(Pet pet) throws PetNotFoundException {
        Pet updatedPet = dao.findById(pet.getId());
        if (pet.getAge() != null) {
            updatedPet.setAge(pet.getAge());
        }

        if (pet.getType() != null) {
            updatedPet.setType(pet.getType());
        }

        if (pet.getDescription() != null) {
            updatedPet.setDescription(pet.getDescription());
        }

        if (pet.getImageUrl() != null) {
            updatedPet.setImageUrl(pet.getImageUrl());
        }

        if (pet.getOwnerEmail() != null) {
            updatedPet.setOwnerEmail(pet.getOwnerEmail());
        }

        if (pet.getSex() != null) {
            updatedPet.setSex(pet.getSex());
        }
        return dao.update(updatedPet);
    }

}
