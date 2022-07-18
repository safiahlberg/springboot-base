package com.wixia.rediscache.persistence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "item")
public class ItemEo implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer price;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("items")
    private CustomerEo owner;

    public ItemEo(String name, Integer price) {
        this.name = name;
        this.price = price;
    }

    public ItemEo(String name, Integer price, CustomerEo owner) {
        this.name = name;
        this.price = price;
        this.owner = owner;
    }

}
