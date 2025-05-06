package com.io.fute.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record GroupInfo(@NotNull UUID id, @NotBlank String name, @NotBlank String location,
                        @PositiveOrZero int numberOfPlayers) {
}
