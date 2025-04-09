package com.io.fute.dto.group;

import jakarta.validation.constraints.NotBlank;

public record GroupRequest(@NotBlank String name, @NotBlank String location) {
}
