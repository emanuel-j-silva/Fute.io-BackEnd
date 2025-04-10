package com.io.fute.dto.player;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record PlayerInfo(@NotBlank String name,
                         @PositiveOrZero @Min(1) @Max(100) byte overall, String urlPhoto) {
}
