package ru.studiotg.minesweeper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.studiotg.minesweeper.dto.GameInfoResponse;
import ru.studiotg.minesweeper.dto.NewGameRequest;
import ru.studiotg.minesweeper.service.GameInfoService;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameInfoController.class)
public class GameControllerTest {

    @MockBean
    GameInfoService gameInfoService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mvc;

    @Test
    void createNewGame() throws Exception {
        NewGameRequest newGameRequest = new NewGameRequest(10, 10, 10);
        GameInfoResponse gameInfoResponse = new GameInfoResponse(UUID.randomUUID().toString(), 10, 10, 10, new String[10][10], false);

        when(gameInfoService.createNewGame(Mockito.any(NewGameRequest.class)))
                .thenReturn(gameInfoResponse);

        mvc.perform(post("/minesweeper/new")
                        .content(objectMapper.writeValueAsString(newGameRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(result -> {
                    String jsonResponse = result.getResponse().getContentAsString();
                    String expectedResponse = objectMapper.writeValueAsString(gameInfoResponse);

                    JSONAssert.assertEquals(expectedResponse, jsonResponse, true);
                });

    }
}
