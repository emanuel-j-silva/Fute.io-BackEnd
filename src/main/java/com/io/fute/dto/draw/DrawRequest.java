package com.io.fute.dto.draw;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record DrawRequest(@NotNull UUID userId, @NotNull UUID groupId,
                          @NotBlank List<Long> playerIds, int numberOfTeams) {
}
