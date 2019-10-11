package com.wimonrat.pet.health;

import com.codahale.metrics.health.HealthCheck;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PetResourceHealthCheck extends HealthCheck {
    private final Client client;

    public PetResourceHealthCheck(Client client) {
        super();
        this.client = client;
    }


    @Override
    public Result check() {
        WebTarget webTarget = client.target("http://localhost:8080/pets");
        Invocation.Builder invocationBuilder =  webTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.get();
        if(response.getStatus() == HttpStatus.OK_200) {
            return Result.healthy();
        }
        return Result.unhealthy(response.readEntity(String.class));

    }
}
