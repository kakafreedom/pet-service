package com.wimonrat.pet.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimonrat.pet.client.PetService;
import com.wimonrat.pet.core.CreatePet;
import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.core.PetType;
import com.wimonrat.pet.core.Sex;
import com.wimonrat.pet.errorcode.ResponseCode;
import com.wimonrat.pet.exception.PetNotFoundException;
import com.wimonrat.pet.exception.mapper.CustomExceptionMapper;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link PetResource}.
 */
public class PetResourceTest {
    private static final PetService petService = mock(PetService.class);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new PetResource(petService))
            .addResource(new CustomExceptionMapper())
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private Pet cat1;

    @Before
    public void setup() {
        cat1 = Pet.builder()
                .age(2)
                .id(1L)
                .description("my lovely cat")
                .imageUrl("http://www.google.com")
                .sex(Sex.F)
                .type(PetType.CAT)
                .build();
    }

    @After
    public void tearDown() {
        reset(petService);
    }

    @Test
    public void searchPetSuccess() {
        when(petService.search(null, PetType.CAT, null, null, null)).thenReturn(Collections.singletonList(cat1));

        List<Pet> pets = resources.client().target("/pets?type=CAT").request().get(new GenericType<List<Pet>>() {
        });

        assertThat(pets).isNotEmpty();
        assertThat(pets).hasSize(1);
        assertThat(pets).contains(cat1);
        verify(petService).search(null, PetType.CAT, null, null, null);
    }

    @Test
    public void createPetSuccess() {
        when(petService.create(anySet())).thenReturn(Collections.singleton(cat1));
        CreatePet createCat = CreatePet.builder().age(cat1.getAge()).description(cat1.getDescription())
                .imageUrl(cat1.getImageUrl()).sex(cat1.getSex()).type(cat1.getType()).build();
        Set<CreatePet> cats = Collections.singleton(createCat);
        final Response response = resources.client().target("/pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(cats, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(petService).create(anySet());
    }

    @Test
    public void updatePetSuccess() throws Exception {
        when(petService.update(anySet())).thenReturn(Collections.singleton(cat1));
        Pet createCat = Pet.builder().id(cat1.getId()).description("updated description").build();
        Set<Pet> cats = Collections.singleton(createCat);
        final Response response = resources.client().target("/pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(cats, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(petService).update(anySet());
    }


    @Test
    public void updatePetNotExistInSystem() throws Exception {
        when(petService.update(anySet())).thenThrow(new PetNotFoundException());
        Pet createCat = Pet.builder().id(cat1.getId()).description("updated description").build();
        Set<Pet> cats = Collections.singleton(createCat);
        final Response response = resources.client().target("/pets")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(cats, MediaType.APPLICATION_JSON_TYPE));

        ByteArrayInputStream inputStream = (ByteArrayInputStream) response.getEntity();
        String actualResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        assertThat(actualResponse).isEqualTo(MAPPER.writeValueAsString(ResponseCode.PET_NOT_FOUND));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NOT_FOUND);
        verify(petService).update(anySet());
    }

}
