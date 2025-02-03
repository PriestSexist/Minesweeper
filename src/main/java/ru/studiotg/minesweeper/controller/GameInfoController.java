package ru.studiotg.minesweeper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.GameTurnRequest;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.service.GameInfoService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/minesweeper")
@CrossOrigin(origins = "https://minesweeper-test.studiotg.ru/")
public class GameInfoController {

    private final GameInfoService gameInfoService;

    /**
     * Метод, для создания новой игры
     *
     * @param newGameRequest запрос на создание новой игшры
     * @return данные о новой игре
     */
    @PostMapping("/new")
    public GameInfoResponse createNewGame(@RequestBody @Valid NewGameRequest newGameRequest) {
        return gameInfoService.createNewGame(newGameRequest);
    }


    /**
     * Метод, для хода в игре
     *
     * @param gameTurnRequest запрос на создание хода
     * @return данные о новом состоянии игры
     */
    @PostMapping("/turn")
    public GameInfoResponse makeTurn(@RequestBody @Valid GameTurnRequest gameTurnRequest) {
        return gameInfoService.makeTurn(gameTurnRequest);
    }
}
