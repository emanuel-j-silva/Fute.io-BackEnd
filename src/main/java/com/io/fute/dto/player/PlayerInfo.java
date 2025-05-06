package com.io.fute.dto.player;

import jakarta.validation.constraints.*;

public record PlayerInfo(@NotNull Long id, @NotBlank String name,
                         @PositiveOrZero @Min(1) @Max(100) byte overall, String urlPhoto) {
}
