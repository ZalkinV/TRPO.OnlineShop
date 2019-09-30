package com.microservices.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;

    @Enumerated(value = EnumType.ORDINAL)
    private OrderStatus orderStatus = OrderStatus.COLLECTING;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="orderId")
    List<OrderItemEntity> orderItemEntities = new ArrayList<>();

}
