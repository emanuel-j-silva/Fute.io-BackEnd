package com.io.fute.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record TokenDTO(@NotBlank String token) {
}
