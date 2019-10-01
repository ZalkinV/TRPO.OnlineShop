package com.microservices.payment.repository;

import com.microservices.payment.entity.PaymentEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends CrudRepository<PaymentEntity, Integer> {
    Optional<PaymentEntity> findByOrderId(Integer orderId);
}
