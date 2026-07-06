package be.raftdev.maestro.exception;

import jakarta.validation.constraints.NotNull;

public record ApiError(@NotNull String error, @NotNull String message, @NotNull String path) {

}
