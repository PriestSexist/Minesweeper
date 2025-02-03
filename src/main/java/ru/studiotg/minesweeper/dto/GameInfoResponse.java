package ru.studiotg.minesweeper.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@Data
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class GameInfoResponse {
    /**
     * Id игры
     */
    private final String game_id;

    /**
     * Ширина поля
     */
    private final int width;

    /**
     * Высота поля
     */
    private final int height;

    /**
     * Количество мин
     */
    private final int mines_count;

    /**
     * Поле, которое видит пользователь
     */
    private final String[][] field;

    /**
     * Флаг, который указывает, что игра завершена
     */
    private final boolean completed;
}
