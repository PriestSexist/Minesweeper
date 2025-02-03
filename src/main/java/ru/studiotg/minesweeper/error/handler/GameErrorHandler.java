package ru.studiotg.minesweeper.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.studiotg.minesweeper.error.exception.GameBadRequestException;
import ru.studiotg.minesweeper.error.model.ErrorResponse;

import java.util.Collections;

@RestControllerAdvice
public class GameErrorHandler {

    @ExceptionHandler(GameBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final Exception e) {
        return new ErrorResponse(Collections.singletonList(e.getMessage()));
    }
}
