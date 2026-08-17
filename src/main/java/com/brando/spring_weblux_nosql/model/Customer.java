package com.brando.spring_weblux_nosql.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customer")
public class Customer {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
