package com.piedraazul.patient.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class LinkAuthUserRequest {

    @NotNull
    private UUID authUserId;
}