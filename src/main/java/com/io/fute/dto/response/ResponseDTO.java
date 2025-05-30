package com.io.fute.dto.response;

import jakarta.validation.constraints.NotBlank;

public record ResponseDTO(@NotBlank String message) {
}
