package ru.studiotg.minesweeper.error.model;

import lombok.Data;

import java.util.List;

@Data
public class ErrorResponse {
    private final List<String> errors;

    public ErrorResponse(List<String> errors) {
        this.errors = errors;

    }
}
