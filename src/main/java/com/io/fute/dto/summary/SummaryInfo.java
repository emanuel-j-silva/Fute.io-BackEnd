package com.io.fute.dto.summary;

import jakarta.validation.constraints.NotNull;

public record SummaryInfo(@NotNull String lastDrawDate, @NotNull String lastDrawGroup,
                          @NotNull Long totalDraws){
}
