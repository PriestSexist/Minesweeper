package ru.studiotg.minesweeper.mapper;

import lombok.experimental.UtilityClass;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.model.GameInfo;

@UtilityClass
public class GameInfoMapper {
    public static GameInfoResponse createGameInfoResponse(GameInfo gameInfo) {
        return GameInfoResponse.builder()
                .game_id(gameInfo.getGame_id().toString())
                .width(gameInfo.getWidth())
                .height(gameInfo.getHeight())
                .mines_count(gameInfo.getMinesCount())
                .field(GameInfo.deserializeField(gameInfo.getOpenField()))
                .completed(gameInfo.isCompleted())
                .build();
    }

    public static GameInfo createGameInfo(NewGameRequest newGameRequest, String initialField, String openField) {
        return GameInfo.builder()
                .width(newGameRequest.getWidth())
                .height(newGameRequest.getHeight())
                .minesCount(newGameRequest.getMines_count())
                .initialField(initialField)
                .openField(openField)
                .build();
    }
}
