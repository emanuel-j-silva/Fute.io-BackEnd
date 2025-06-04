package com.io.fute.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserInfo(@NotBlank String name) {

}
