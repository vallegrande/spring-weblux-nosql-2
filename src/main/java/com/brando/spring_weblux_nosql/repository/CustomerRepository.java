package com.brando.spring_weblux_nosql.repository;

import com.brando.spring_weblux_nosql.model.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
}
