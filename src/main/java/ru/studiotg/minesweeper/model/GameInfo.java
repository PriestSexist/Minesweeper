package ru.studiotg.minesweeper.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_info")
public class GameInfo {

    /**
     * ID игры
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID game_id;

    /**
     * Ширина поля
     */
    @Column(name = "width")
    private int width;

    /**
     * Высота поля
     */
    @Column(name = "height")
    private int height;

    /**
     * Количество мин
     */
    @Column(name = "mines_count")
    private int minesCount;

    /**
     * Флаг, указывающий на то, что игра завершена
     */
    @Column(name = "completed")
    private boolean completed = false;

    /**
     * Игровое поле (с минами и цифрами)
     */
    @Column(name = "initial_field", columnDefinition = "jsonb")
    private String initialField;

    /**
     * Открытое поле (то, которое видит пользователь)
     */
    @Column(name = "open_field", columnDefinition = "jsonb")
    private String openField;

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
