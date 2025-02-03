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
    /**
     * Обработка ошибки, когда пользователь передает неверные данные
     *
     * @param gameBadRequestException - ошибка
     * @return - ответ, который отправляется пользователю
     */
    @ExceptionHandler(GameBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(final Exception gameBadRequestException) {
        return new ErrorResponse(Collections.singletonList(gameBadRequestException.getMessage()));
    }
}
