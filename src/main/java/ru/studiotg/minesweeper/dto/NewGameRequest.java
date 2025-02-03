package ru.studiotg.minesweeper.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import ru.studiotg.minesweeper.error.constants.ErrorStrings;


@Data
@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class NewGameRequest {

    /**
     * Ширина поля (2-30)
     */
    @Range(min = 2, max = 30, message = ErrorStrings.INVALID_NEW_GAME_WIDTH)
    private final int width;

    /**
     * Высота поля (2-30)
     */
    @Range(min = 2, max = 30, message = ErrorStrings.INVALID_NEW_GAME_HEIGHT)
    private final int height;

    /**
     * Количество мин.
     */
    private final int mines_count;
}
