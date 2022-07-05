package com.wixia.rediscache.persistence;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "customer")
public class CustomerEo {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "owner")
    private List<ItemEo> items;

    protected CustomerEo() {}

    public CustomerEo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.items = List.of();
    }

    public CustomerEo(String firstName, String lastName, @NonNull List<ItemEo> items) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.items = items;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, firstName='%s', lastName='%s', items=['%s']]",
                id, firstName, lastName,
            items.stream().map(ItemEo::toString).collect(Collectors.joining(", ", "[", "]")));
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<ItemEo> getItems() {
        return items;
    }

}
