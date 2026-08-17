package com.brando.spring_weblux_nosql.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Bean
    @Primary
    public MongoClientSettings mongoClientSettings() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        return MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
    }

    @Bean
    @Primary
    public MongoClient mongoClient(MongoClientSettings settings) {
        System.out.println();
        System.out.println("========================================================");
        System.out.println("  MongoConfig: Creando MongoClient EXPLICITO");
        System.out.println("  URI            = " + mongoUri);
        System.out.println("  Hosts reales   = " + settings.getClusterSettings().getHosts());
        System.out.println("  Modo cluster   = " + settings.getClusterSettings().getMode());
        System.out.println("  Tipo requerido = " + settings.getClusterSettings().getRequiredClusterType());
        System.out.println("  Credencial     = " + (settings.getCredential() != null ? "CONFIGURADA" : "NULA (cuidado!)"));
        System.out.println("========================================================");
        System.out.println();
        return MongoClients.create(settings);
    }

    @Bean
    @Primary
    public ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory(MongoClient mongoClient) {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        String database = connectionString.getDatabase() != null ? connectionString.getDatabase() : "test";
        return new SimpleReactiveMongoDatabaseFactory(mongoClient, database);
    }

    @Bean
    @Primary
    public ReactiveMongoTemplate reactiveMongoTemplate(ReactiveMongoDatabaseFactory factory) {
        return new ReactiveMongoTemplate(factory);
    }
}
