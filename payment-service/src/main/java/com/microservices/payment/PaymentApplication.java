package com.microservices.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Enabling Swagger allows to see all {@link com.microservices.payment.controller.PaymentController} endpoints
 * and send request using UI.
 * <br/>
 * Swagger configuration in json format: http://{host}:{port}/v2/api-docs
 * <br/>
 * Swagger UI: http://{host}:{port}/swagger-ui.html
 */
@SpringBootApplication
@EnableSwagger2
@EnableEurekaClient
public class PaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }

}
