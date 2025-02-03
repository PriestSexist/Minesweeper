package ru.studiotg.minesweeper.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.model.GameInfo;
import ru.studiotg.minesweeper.repository.GameInfoRepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameInfoServiceTest {

    @Mock
    GameInfoRepository gameInfoRepository;

    GameInfoService gameInfoService;

    @BeforeEach
    void generator() {
        gameInfoService = new GameInfoService(gameInfoRepository);
    }

    @Test
    void createNewGameTest() {
        String[][] initialField= new String[10][10];
        String[][] openField = new String[10][10];
        String uuidRegex = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

        for (int i = 0; i < 10; i++) {
            Arrays.fill(openField[i] = new String[10], " ");
        }
        NewGameRequest newGameRequest = new NewGameRequest(10, 10, 10);

        Mockito.when(gameInfoRepository.save(Mockito.any(GameInfo.class)))
                .thenReturn(new GameInfo(UUID.randomUUID(), 10, 10, 10, false, serializeField(initialField), serializeField(openField)));


        GameInfoResponse gameInfoResponse = gameInfoService.createNewGame(newGameRequest);

        assertTrue(gameInfoResponse.getGame_id().matches(uuidRegex));
        assertEquals(10, gameInfoResponse.getWidth());
        assertEquals(10, gameInfoResponse.getHeight());
        assertEquals(10, gameInfoResponse.getMines_count());
        assertEquals(10, gameInfoResponse.getField().length);
        assertEquals(10, gameInfoResponse.getField()[0].length);

    }

    @Test
    void generateInitialFieldTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // Получение доступа к приватному методу
        Method privateMethod = GameInfoService.class.getDeclaredMethod("generateInitialField", int.class, int.class, int.class);
        privateMethod.setAccessible(true);
        // Вызов метода generateInitialField
        String[][] initialFieldFromService = deserializeField((String) privateMethod.invoke(gameInfoService, 10, 10, 10));


        // Проверка расположения мин
        for (int i = 0; i < initialFieldFromService.length; i++) {
            for (int j = 0; j < initialFieldFromService[i].length; j++) {
                if (!initialFieldFromService[i][j].equals("M") && !initialFieldFromService[i][j].matches("[0-8]")) {
                    fail();
                }
            }
        }
    }

    @Test
    void generateOpenFiledTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[][] openField = new String[10][10];

        // Создание видимого поля
        for (int i = 0; i < 10; i++) {
            Arrays.fill(openField[i] = new String[10], " ");
        }

        // Получение доступа к приватному методу
        Method privateMethod = GameInfoService.class.getDeclaredMethod("generateOpenFiled", int.class, int.class);
        privateMethod.setAccessible(true);
        // Вызов метода generateOpenFiled
        String[][] openFieldFromService = deserializeField((String) privateMethod.invoke(gameInfoService, 10, 10));

        assertTrue(Arrays.deepEquals(openField, openFieldFromService));
    }

    @Test
    void countAdjacentMinesTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        // Создание тестового поля
        String[][] field = new String[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = ".";
            }
        }

        // Размещение мин на поле
        field[2][4] = "M";
        field[2][5] = "M";
        field[2][6] = "M";

        // Получение доступа к приватному методу
        Method privateMethod = GameInfoService.class.getDeclaredMethod("countAdjacentMines", String[][].class, int.class, int.class);
        privateMethod.setAccessible(true);
        // Вызов метода countAdjacentMines
        int count0 = (int) privateMethod.invoke(gameInfoService, field, 3, 2);
        int count1 = (int) privateMethod.invoke(gameInfoService, field, 3, 3);
        int count2 = (int) privateMethod.invoke(gameInfoService, field, 3, 4);
        int count3 = (int) privateMethod.invoke(gameInfoService, field, 3, 5);

        assertEquals(0, count0);
        assertEquals(1, count1);
        assertEquals(2, count2);
        assertEquals(3, count3);
    }

    public static String[][] deserializeField(String field) {
        String[][] programField;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            programField = objectMapper.readValue(field, String[][].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка во время десериализации поля", e);
        }
        return programField;
    }

    public static String serializeField(String[][] field) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(field);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Ошибка во время сериализации поля", e);
        }
    }
}