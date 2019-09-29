package com.microservices.item.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name="items")
@NoArgsConstructor
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long entityId;
    @NotEmpty(message = "please provide a name")
    private String name;
    @NotNull(message = "Please provide a price")
    @DecimalMin("1.00")
    private BigDecimal price;
    @NotNull(message = "Please provide an amount")
    @DecimalMin("1.00")
    private Long availableAmount;
}
