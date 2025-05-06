package com.io.fute.dto.draw;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record DrawRequest(@NotNull @Size(min=3) List<Long> playerIds, int numberOfTeams) {
}
