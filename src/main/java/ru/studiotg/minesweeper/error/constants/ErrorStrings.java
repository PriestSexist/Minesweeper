package ru.studiotg.minesweeper.error.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorStrings {
    public static final String INVALID_NEW_GAME_WIDTH = "Ширина поля должна быть не менее 2 и не более 30";
    public static final String INVALID_NEW_GAME_HEIGHT = "Высота поля должна быть не менее 2 и не более 30";
    public static final String INVALID_MAKE_TURN_WIDTH = "Выбранная колонка должна быть не менее 0 и не более 29";
    public static final String INVALID_MAKE_TURN_HEIGHT = "Выбранная строка должна быть не менее 0 и не более 29";
}
