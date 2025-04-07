package com.io.fute.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record GroupInfo(@NotBlank String name, @NotBlank String location,
                        @PositiveOrZero int numberOfPlayers) {
}
