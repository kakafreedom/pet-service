package com.wimonrat.pet;

import com.wimonrat.pet.client.IPetService;
import com.wimonrat.pet.client.PetService;
import com.wimonrat.pet.core.Pet;
import com.wimonrat.pet.db.PetDAO;
import com.wimonrat.pet.exception.mapper.ConstraintViolationExceptionMapper;
import com.wimonrat.pet.exception.mapper.CustomExceptionMapper;
import com.wimonrat.pet.exception.mapper.GenericExceptionMapper;
import com.wimonrat.pet.health.PetResourceHealthCheck;
import com.wimonrat.pet.resources.PetResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.client.JerseyClientBuilder;

import javax.ws.rs.client.Client;


public class PetApplication extends Application<PetConfiguration> {

    public static void main(String[] args) throws Exception {
        new PetApplication().run(args);
    }

    private final HibernateBundle<PetConfiguration> hibernateBundle =
            new HibernateBundle<PetConfiguration>(Pet.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(PetConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    @Override
    public String getName() {
        return "pet-application";
    }

    @Override
    public void initialize(Bootstrap<PetConfiguration> bootstrap) {

        bootstrap.addBundle(new MigrationsBundle<PetConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(PetConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);

    }

    @Override
    public void run(PetConfiguration configuration, Environment environment) throws Exception {
        //Remove all default ExceptionMappers
        final PetDAO dao = new PetDAO(hibernateBundle.getSessionFactory());

        // Exception Mapper
        ((DefaultServerFactory) configuration.getServerFactory()).setRegisterDefaultExceptionMappers(false);
        environment.jersey().register(new CustomExceptionMapper());
        environment.jersey().register(new ConstraintViolationExceptionMapper());
        environment.jersey().register(new GenericExceptionMapper());

        // Pet Service
        final IPetService petService = new PetService(dao);
        final PetResource petResource = new PetResource(petService);
        environment.jersey().register(petResource);

        // Pet Resource Health Check
        final Client client = new JerseyClientBuilder().build();
        final PetResourceHealthCheck healthCheck = new PetResourceHealthCheck(client);
        environment.healthChecks().register("petResource", healthCheck);

    }
}

