package com.brando.spring_weblux_nosql.controller;

import com.brando.spring_weblux_nosql.model.Customer;
import com.brando.spring_weblux_nosql.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
@Tag(name = "Customer API", description = "Reactive CRUD operations for customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Create a customer")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> create(@Valid @RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @Operation(summary = "Get all customers")
    @GetMapping
    public Flux<Customer> findAll() {
        return customerService.findAll();
    }

    @Operation(summary = "Get a customer by id")
    @GetMapping("/{id}")
    public Mono<Customer> findById(@PathVariable String id) {
        return customerService.findById(id);
    }

    @Operation(summary = "Update a customer by id")
    @PutMapping("/{id}")
    public Mono<Customer> update(@PathVariable String id, @Valid @RequestBody Customer customer) {
        return customerService.update(id, customer);
    }

    @Operation(summary = "Delete a customer by id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable String id) {
        return customerService.delete(id);
    }
}
