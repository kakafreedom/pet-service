package com.wimonrat.pet;

import com.google.common.collect.Lists;
import com.wimonrat.pet.core.CreatePet;
import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.core.PetType;
import com.wimonrat.pet.core.Sex;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.assertj.core.api.Assertions;
import org.junit.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class IntegrationTest {

    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-service.yml");

    @ClassRule
    public static final DropwizardAppRule<PetConfiguration> RULE = new DropwizardAppRule<>(
            PetApplication.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    private Client client;

    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    @Before
    public void setUp() {
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown() {
        client.close();
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-service", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testGetPostUpdatePet() throws Exception {

        //POST
        final CreatePet cat = CreatePet.builder()
                .age(2)
                .description("my lovely cat")
                .imageUrl("http://www.google.com")
                .sex(Sex.F)
                .type(PetType.CAT)
                .build();
        final Set<CreatePet> pets = Collections.singleton(cat);
        Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/pets")
                .request()
                .post(Entity.entity(pets, MediaType.APPLICATION_JSON_TYPE));
        Assertions.assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        Set<Pet> newPets = response.readEntity(new GenericType<Set<Pet>>() {
        });
        assertThat(newPets).isNotEmpty();
        assertThat(newPets).size().isEqualTo(1);
        newPets.forEach(newPet -> {
            assertThat(newPet.getId()).isNotNull();
            assertThat(newPet.getCreatedDate()).isNotNull();
            assertThat(newPet.getAge()).isEqualTo(cat.getAge());
            assertThat(newPet.getDescription()).isEqualTo(cat.getDescription());
        });

        //UPDATE
        Pet newPet = Lists.newArrayList(newPets).get(0);
        final Pet updatedCat = Pet.builder()
                .id(newPet.getId())
                .description("update description")
                .build();
        final Set<Pet> updatedPets = Collections.singleton(updatedCat);
        response = client.target("http://localhost:" + RULE.getLocalPort() + "/pets")
                .request()
                .put(Entity.entity(updatedPets, MediaType.APPLICATION_JSON_TYPE));
        Assertions.assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        Set<Pet> updatedPetsResult = response.readEntity(new GenericType<Set<Pet>>() {
        });
        assertThat(updatedPetsResult).isNotEmpty();
        assertThat(updatedPetsResult).size().isEqualTo(1);
        updatedPetsResult.forEach(updatedPet -> {
            assertThat(updatedPet.getId()).isEqualTo(newPet.getId());
            assertThat(updatedPet.getUpdatedDate()).isNotNull();
            assertThat(updatedPet.getAge()).isEqualTo(cat.getAge());
            assertThat(updatedPet.getDescription()).isEqualTo(updatedCat.getDescription());
            assertThat(updatedPet.getType()).isEqualTo(newPet.getType());
        });

        //SEARCH
        Pet updatedPet = Lists.newArrayList(updatedPetsResult).get(0);

        response = client.target("http://localhost:" + RULE.getLocalPort() + "/pets")
                .queryParam("type", "CAT")
                .request()
                .get();
        Assertions.assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        Set<Pet> foundPets = response.readEntity(new GenericType<Set<Pet>>() {
        });
        assertThat(foundPets).isNotEmpty();
        assertThat(foundPets).size().isEqualTo(1);
        Pet foundPet = Lists.newArrayList(foundPets).get(0);
        assertThat(foundPet).isEqualTo(updatedPet);
    }

    @Test
    public void testUpdatePetMissingIdField() throws Exception {
        final Pet cat = Pet.builder()
                .age(2)
                .description("my lovely cat")
                .imageUrl("http://www.google.com")
                .sex(Sex.F)
                .type(PetType.CAT)
                .build();
        final Set<Pet> pets = Collections.singleton(cat);
        final Response response = client.target("http://localhost:" + RULE.getLocalPort() + "/pets")
                .request()
                .put(Entity.entity(pets, MediaType.APPLICATION_JSON_TYPE));
        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.BAD_REQUEST);
        String responseText = response.readEntity(String.class);
        assertThat(responseText).isEqualTo("{\"errorCode\":\"MISSING_REQUIRED_FIELDS\",\"message\":\"Id is required.\"}");
    }

}
