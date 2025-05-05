package com.io.fute.dto.team;

import com.io.fute.dto.player.PlayerInfo;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record TeamInfo(@NotBlank String numeralName, @NotBlank List<PlayerInfo> players) {
}
