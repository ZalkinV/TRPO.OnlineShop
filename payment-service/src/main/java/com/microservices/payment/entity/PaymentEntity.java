package com.microservices.payment.entity;

import javax.persistence.*;

import com.microservices.payment.dto.PaymentDto;
import lombok.*;

@Entity
@Table(name = "payment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int orderId;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus status;

    public PaymentEntity(int orderId) {
        this.orderId = orderId;
        this.status = PaymentStatus.PENDING;
    }

    public PaymentDto toPaymentDto() {
        return new PaymentDto(id, orderId, status);
    }
}
