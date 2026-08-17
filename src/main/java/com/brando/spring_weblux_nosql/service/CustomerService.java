package com.brando.spring_weblux_nosql.service;

import com.brando.spring_weblux_nosql.exception.CustomerNotFoundException;
import com.brando.spring_weblux_nosql.model.Customer;
import com.brando.spring_weblux_nosql.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Mono<Customer> create(Customer customer) {
        return customerRepository.save(customer);
    }

    public Flux<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Mono<Customer> findById(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)));
    }

    public Mono<Customer> update(String id, Customer customer) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)))
                .flatMap(existing -> {
                    if (customer.getFirstName() != null) existing.setFirstName(customer.getFirstName());
                    if (customer.getLastName() != null) existing.setLastName(customer.getLastName());
                    if (customer.getEmail() != null) existing.setEmail(customer.getEmail());
                    if (customer.getPhone() != null) existing.setPhone(customer.getPhone());
                    return customerRepository.save(existing);
                });
    }

    public Mono<Void> delete(String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)))
                .flatMap(existing -> customerRepository.deleteById(id));
    }
}
