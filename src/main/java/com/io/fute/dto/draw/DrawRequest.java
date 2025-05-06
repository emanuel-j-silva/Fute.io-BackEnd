package com.io.fute.dto.draw;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record DrawRequest(@NotBlank List<Long> playerIds, int numberOfTeams) {
}
