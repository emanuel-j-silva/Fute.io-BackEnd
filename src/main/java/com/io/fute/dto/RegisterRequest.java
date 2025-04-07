package com.io.fute.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank String name, @NotBlank String username,
                              @NotBlank String email,@NotBlank String password) {
}
