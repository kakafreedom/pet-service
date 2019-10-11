package com.wimonrat.pet.db;

import com.google.common.base.Strings;
import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.core.PetType;
import com.wimonrat.pet.core.Sex;
import com.wimonrat.pet.exception.PetNotFoundException;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class PetDAO extends AbstractDAO<Pet> implements IPetDAO{

    public PetDAO(SessionFactory factory) {
        super(factory);
    }

    @Override
    public Pet create(Pet pet) {
        pet.setDeleted(false);
        return persist(pet);
    }

    @Override
    public Pet update(Pet pet) {
        return persist(pet);
    }

    @Override
    public void delete(Pet pet) {
        pet.setDeleted(true);
        super.currentSession().delete(pet);
    }

    @Override
    public Pet findById(Long id) throws PetNotFoundException {
        List<Pet> pets = list(namedQuery("Pet.findById").setParameter("id", id));
        if(pets.isEmpty()) {
             throw new PetNotFoundException();
        }
        return pets.get(0);
    }

    @Override
    public List<Pet> search(Long id, PetType type, Integer age, String ownerEmail, Sex sex){
        CriteriaBuilder cb = super.currentSession().getCriteriaBuilder();
        CriteriaQuery<Pet> criteriaQuery = cb.createQuery(Pet.class);

        Root<Pet> root = criteriaQuery.from(Pet.class);

        List<Predicate> predicates = new ArrayList<>();

        if (id != null) {
            predicates.add(cb.equal(root.get("id"), id));
        }

        if (type != null) {
            predicates.add(cb.equal(root.get("type"), type));
        }

        if (sex != null) {
            predicates.add(cb.equal(root.get("sex"), sex));
        }

        if (age != null) {
            predicates.add(cb.equal(root.get("age"), age));
        }

        if (!Strings.isNullOrEmpty(ownerEmail)) {
            predicates.add(cb.equal(root.get("ownerEmail"), ownerEmail));
        }

        predicates.add(cb.isFalse(root.get("isDeleted")));
        criteriaQuery.select(root).where(predicates.stream().toArray(Predicate[]::new));

        Query query = super.currentSession().createQuery(criteriaQuery);

        return query.getResultList();
    }
}
