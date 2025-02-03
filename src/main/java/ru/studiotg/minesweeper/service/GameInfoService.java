package ru.studiotg.minesweeper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.studiotg.minesweeper.error.exception.GameBadRequestException;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.GameTurnRequest;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.mapper.GameInfoMapper;
import ru.studiotg.minesweeper.model.GameInfo;
import ru.studiotg.minesweeper.repository.GameInfoRepository;

import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class GameInfoService {

    private final GameInfoRepository gameInfoRepository;

    /**
     * Метод, для создания новой игры
     *
     * @param newGameRequest запрос на создание новой игры.
     * @return данные о новой игре
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public GameInfoResponse createNewGame(NewGameRequest newGameRequest) {
        log.info("Начинаем создавать новую игру");
        String initialField = generateInitialField(newGameRequest.getHeight(), newGameRequest.getWidth(), newGameRequest.getMines_count());
        String  openField = generateOpenFiled(newGameRequest.getHeight(), newGameRequest.getWidth());

        GameInfo gameInfo = gameInfoRepository.save(GameInfoMapper.createGameInfo(newGameRequest, initialField, openField));
        log.info("Сохраняем игру с id {}", gameInfo.getGame_id());
        return GameInfoMapper.createGameInfoResponse(gameInfo);
    }

    /**
     * Метод, для хода в игре
     *
     * @param gameTurnRequest запрос на создание хода
     * @return данные о новом состоянии игры
     * @throws GameBadRequestException если игры с таким id не существует
     * @throws GameBadRequestException если игра уже завершена
     * @throws GameBadRequestException если координаты поля не верны
     * @throws GameBadRequestException если поле уже открыто
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public GameInfoResponse makeTurn(GameTurnRequest gameTurnRequest) {
        log.info("Начинаем создавать новый ход");
        GameInfo gameInfo = gameInfoRepository.findById(UUID.fromString(gameTurnRequest.getGame_id()))
                .orElseThrow(() -> new GameBadRequestException(String.format("Игра с id %s не найдена", gameTurnRequest.getGame_id())));
        log.info("Игра найдена: {}", gameInfo);

        if (gameInfo.isCompleted()) {
            throw new GameBadRequestException(String.format("Игра с идентификатором %s уже завершена", gameTurnRequest.getGame_id()));
        }

        String[][] openField = GameInfo.deserializeField(gameInfo.getOpenField());
        String[][] initialField = GameInfo.deserializeField(gameInfo.getInitialField());
        int row = gameTurnRequest.getRow();
        int col = gameTurnRequest.getCol();

        if (row >= initialField.length || col >= initialField[0].length) {
            throw new GameBadRequestException(String.format("Неверные координаты поля. Ширина поля %d, высота поля %d", gameInfo.getWidth(), gameInfo.getHeight()));
        }

        if (!openField[row][col].equals(" ")) {
            throw new GameBadRequestException(String.format("Поле с шириной %d и высотой %d уже открыто", row, col));
        }

        openCell(openField, initialField, row, col);
        checkGameCompletion(gameInfo, row, col, openField, initialField);
        gameInfo.setOpenField(GameInfo.serializeField(openField));
        gameInfoRepository.save(gameInfo);
        log.info("Сохраняем игру с id {}", gameInfo.getGame_id());
        return GameInfoMapper.createGameInfoResponse(gameInfo);
    }

    /**
     * Открывает клетку поля
     * @param openField - открытое поле
     * @param initialField - поле с минами
     * @param row - ряд
     * @pТутaram col - колонка
     */
    private void openCell(String[][] openField, String[][] initialField, int row, int col) {
        log.info("Открываем клетку поля (ряд {}, колонка {})", row, col);
        openField[row][col] = initialField[row][col];

        if (openField[row][col].equals("0")) {
            log.info("Клетка пустая, открываем соседние клетки");
            for (int i = Math.max(0, row - 1); i <= Math.min(initialField.length - 1, row + 1); i++) {
                for (int j = Math.max(0, col - 1); j <= Math.min(initialField[0].length - 1, col + 1); j++) {
                    if (i == row && j == col) continue;
                    if (initialField[i][j].equals("0") && openField[i][j].equals(" ")) {
                        openCell(openField, initialField, i, j);
                    } else {
                        openField[i][j] = initialField[i][j];
                    }
                }
            }
        }
    }

    /**
     * Метод, который проверяет, была ли игра завершена
     * <p>
     * Он считает количество открытых клеток, не содержащих мин, и
     * сравнивает его с общим количеством клеток, за вычетом количества мин.
     * Если эти значения совпадают, то игра считается завершенной
     * </p>
     *
     * @param gameInfo - данные об игре
     */
    private void checkGameCompletion(GameInfo gameInfo, int row, int col, String[][] openField, String[][] initialField) {
        log.info("Проверяем, была ли игра завершена");
        int minesCount = gameInfo.getMinesCount();
        int openedCount = 0;

        if (openField[row][col].equals("M")) {
            gameInfo.setCompleted(true);
            log.info("Меняю все M на X");
            for (int i = 0; i < openField.length; i++) {
                for (int j = 0; j < openField[0].length; j++) {
                    if (initialField[i][j].equals("M")) {
                        openField[i][j] = "X";
                    }
                    if (openField[i][j].equals(" ")) {
                        openField[i][j] = initialField[i][j];
                    }
                }
            }
            return;
        }

        log.info("Начинаю подсчёт открытых клеток");
        int totalCells = gameInfo.getHeight() * gameInfo.getWidth();
        for (String[] strings : openField) {
            for (int j = 0; j < openField[0].length; j++) {
                if (!strings[j].equals(" ") && !strings[j].equals("M")) {
                    openedCount++;
                }
            }
        }
        log.info("Количество открытых клеток: {}", openedCount);

        if (openedCount == totalCells - minesCount) {
            for (int i = 0; i < openField.length; i++) {
                for (int j = 0; j < openField[0].length; j++) {
                    if (openField[i][j].equals(" ")) {
                        openField[i][j] = initialField[i][j];
                    }
                }
            }
            gameInfo.setCompleted(true);
        }
        log.info("Игра завершена? {}", gameInfo.isCompleted());
    }


    /**
     * Метод, для создания поля, которое открывает пользователь
     *
     * @param height - высота
     * @param width  - ширина
     * @return поле, которое открывает пользователь. Оно изначально полностью закрыто (состоит из пробелов)
     */
    private String generateOpenFiled(int height, int width) {
        log.info("Начало генерации  поля, которое открывает пользователь");
        String[][] openField = new String[height][width];

        for (int i = 0; i < height; i++) {
            Arrays.fill(openField[i] = new String[width], " ");
        }

        log.info("Завершение генерации  поля, которое открывает пользователь");
        return GameInfo.serializeField(openField);
    }

    /**
     * Метод, для создания игрового поля (с минами и номерами)
     *
     * @param height     - высота
     * @param width      - ширина
     * @param minesCount - количество мин
     * @return игровое поле заполненное минами и цифрами с количеством мин на соседних клетках
     * @throws GameBadRequestException если количество мин не соответствует условиям
     */
    private String generateInitialField(int height, int width, int minesCount) {
        log.info("Начало генерации игрового поля (с минами и номерами)");
        if (minesCount >= height * width) {
            throw new GameBadRequestException(String.format("количество мин должно быть не менее 1 и не более %d", (height * width - 1)));
        }

        String[][] field = new String[height][width];
        Random random = new Random();
        int minesPlaced = 0;

        for (int i = 0; i < height; i++) {
            Arrays.fill(field[i] = new String[width], " ");
        }

        while (minesPlaced < minesCount) {
            int randRow = random.nextInt(height);
            int randCol = random.nextInt(width);
            if (!field[randRow][randCol].equals("M")) {
                field[randRow][randCol] = "M";
                minesPlaced++;
            }
        }
        log.info("Начало подсчёта мин на соседних клетках");
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (!field[i][j].equals("M")) {
                    field[i][j] = Integer.toString(countAdjacentMines(field, i, j));
                }
            }
        }
        log.info("Завершение подсчёта мин на соседних клетках");
        log.info("Завершение генерации игрового поля (с минами и номерами)");
        return GameInfo.serializeField(field);
    }

    /**
     * Метод, для подсчёта мин на соседних клетках
     *
     * @param field - поле в которое мы ведём подсчёт
     * @param row   - ряд с миной
     * @param col   - колонка с миной
     * @return количество мин на соседних клетках
     */
    private int countAdjacentMines(String[][] field, int row, int col) {

        int count = 0;
        for (int i = Math.max(0, row - 1); i <= Math.min(field.length - 1, row + 1); i++) {
            for (int j = Math.max(0, col - 1); j <= Math.min(field[0].length - 1, col + 1); j++) {
                if (field[i][j].equals("M")) {
                    count++;
                }
            }
        }
        return count;
    }
}
