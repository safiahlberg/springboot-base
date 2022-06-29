package com.wixia.rediscache.resource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.RepresentationModel;

public class CustomerRepresentation extends RepresentationModel<CustomerRepresentation> {

    private final String firstName;
    private final String lastName;

    @JsonCreator
    public CustomerRepresentation(@JsonProperty String firstName, @JsonProperty String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
