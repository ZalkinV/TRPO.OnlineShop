package com.microservices.order.dto;

import lombok.Getter;

@Getter
public class UserDetailsDto {
    String username;

    CardAuthorizationInfo cardAuthorizationInfo;
}
