package ru.studiotg.minesweeper.error.exception;

public class GameBadRequestException extends RuntimeException {
    public GameBadRequestException(String message) {
        super(message);
    }
}
