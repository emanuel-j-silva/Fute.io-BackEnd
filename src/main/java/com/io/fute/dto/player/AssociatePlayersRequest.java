package com.io.fute.dto.player;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AssociatePlayersRequest(@NotNull @Size(min = 1) List<Long> playerIds) {
}
