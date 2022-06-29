package com.wixia.rediscache.resource;

import com.wixia.rediscache.persistence.CustomerEo;
import com.wixia.rediscache.persistence.CustomerRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CustomerController {

    private final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @RequestMapping("customer")
    public HttpEntity<List<CustomerRepresentation>> customer(
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName) {

        final List<CustomerEo> customerEo = findCustomers(firstName, lastName);

        final List<CustomerRepresentation> customerRepresentations = mapCustomers(customerEo);

        return new ResponseEntity<>(customerRepresentations, HttpStatus.OK);
    }

    private List<CustomerRepresentation> mapCustomers(List<CustomerEo> customerEo) {
        final List<CustomerRepresentation> customerRepresentations = customerEo.stream().map(
                ceo -> new CustomerRepresentation(ceo.getFirstName(), ceo.getLastName())).collect(Collectors.toList());
        return customerRepresentations;
    }

    private List<CustomerEo> findCustomers(String firstName, String lastName) {
        if (firstName == null && lastName == null) {
            return customerRepository.findAll();
        } else if (lastName == null) {
            return customerRepository.findByFirstName(firstName);
        } else if (firstName == null) {
            return customerRepository.findByLastName(lastName);
        } else {
            return customerRepository.findByFirstNameAndLastName(firstName, lastName);
        }
    }
}
