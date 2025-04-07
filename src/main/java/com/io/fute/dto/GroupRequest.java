package com.io.fute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GroupRequest(@NotNull UUID userId, @NotBlank String name, @NotBlank String location) {
}
