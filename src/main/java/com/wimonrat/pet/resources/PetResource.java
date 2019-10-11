package com.wimonrat.pet.resources;

import com.wimonrat.pet.core.*;
import com.wimonrat.pet.exception.MissingFieldException;
import com.wimonrat.pet.exception.PetNotFoundException;
import com.wimonrat.pet.client.IPetService;
import io.dropwizard.hibernate.UnitOfWork;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Path("/pets")
@Produces(MediaType.APPLICATION_JSON)
@Consumes({MediaType.APPLICATION_JSON})
public class PetResource {
    private final IPetService petService;

    public PetResource(IPetService petService) {
        this.petService = petService;
    }

    @GET
    @UnitOfWork
    public List<Pet> search(@QueryParam("id") Long id,
                            @QueryParam("type") PetType type,
                            @QueryParam("age") Integer age,
                            @QueryParam("owner_email") String ownerEmail,
                            @QueryParam("sex") Sex sex) {

        return petService.search(id, type, age, ownerEmail, sex);
    }

    @POST
    @UnitOfWork
    public Response create(@Valid Set<CreatePet> pets) {
        Set<Pet> createdPets = petService.create(pets);
        return Response.status(Response.Status.CREATED).entity(createdPets).build();
    }

    @PUT
    @UnitOfWork
    public Set<Pet> update(@Valid Set<Pet> pets) throws PetNotFoundException, MissingFieldException {
        return petService.update(pets);
    }

    @DELETE
    @UnitOfWork
    public void delete(IdWrapper idWrapper) throws PetNotFoundException {
        petService.delete(idWrapper.getIds());
    }


}
