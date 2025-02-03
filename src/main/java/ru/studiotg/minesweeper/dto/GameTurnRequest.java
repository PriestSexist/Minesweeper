package ru.studiotg.minesweeper.dto;

import jakarta.validation.constraints.NotBlank;
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
public class GameTurnRequest {
    /**
     * Id игры, которую нужно продолжить
     */
    @NotBlank
    private final String game_id;

    /**
     * Номер колонки, которую нужно открыть (0-29)
     */
    @Range(min = 0, max = 29, message = ErrorStrings.INVALID_MAKE_TURN_WIDTH)
    private final int col;

    /**
     * Номер строки, которую нужно открыть (0-29)
     */
    @Range(min = 0, max = 29, message = ErrorStrings.INVALID_MAKE_TURN_HEIGHT)
    private final int row;
}
