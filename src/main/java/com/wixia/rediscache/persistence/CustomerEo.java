package com.wixia.rediscache.persistence;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "customer")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "firstName", "lastName" }) })
public class CustomerEo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    // EAGER is needed, or else we get in trouble when re-creating from cache
    private List<ItemEo> items;

    public CustomerEo(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.items = new ArrayList<>();
    }

    public CustomerEo(String firstName, String lastName, @NonNull List<ItemEo> items) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.items = items;
    }

}
