package ru.mephi.trainer.rest.dto.request.task;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Задание на поиск ошибок")
public class ErrorFindingTaskRequest extends TaskRequest {

    @Schema(description = "Текст вопроса",
            examples = "Найдите недостатки или риски валидации.",
            required = true)
    @NotBlank(message = "Текст вопроса не может быть пустым")
    @Size(min = 5, max = 1000, message = "Текст вопроса должен быть от 5 до 1000 символов")
    private String question;

    @Schema(description = "Контекст задания (описание ситуации)",
            examples = "Для поля ИНН организации в форме партнёрства заданы правила: только цифры, не может начинаться с 0...",
            required = true)
    @NotBlank(message = "Контекст задания не может быть пустым")
    @Size(min = 10, max = 5000, message = "Контекст должен быть от 10 до 5000 символов")
    private String context;

    @Schema(description = "Варианты ответов (возможные ошибки)",
            required = true)
    @NotEmpty(message = "Должен быть хотя бы один вариант ответа")
    @Size(min = 2, max = 20, message = "Количество вариантов должно быть от 2 до 20")
    @Valid
    private List<AnswerChoiceDto> answerChoices;

    @Schema(description = "Список порядковых номеров правильных ответов",
            examples = "[1, 2, 4]",
            required = true)
    @NotEmpty(message = "Необходимо указать хотя бы один правильный ответ")
    private List<@Min(1) Integer> expectedOrdinals;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "15",
            required = true)
    @NotNull(message = "Количество баллов не может быть пустым")
    @Positive(message = "Количество баллов должно быть положительным числом")
    @Max(value = 100, message = "Количество баллов не может превышать 100")
    private Integer points;

    @Schema(description = "Штраф за неправильный ответ (в баллах)",
            examples = "3",
            required = true)
    @NotNull(message = "Штраф за ошибку не может быть пустым")
    @Min(value = 0, message = "Штраф не может быть отрицательным")
    @Max(value = 100, message = "Штраф не может превышать 100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток",
            examples = "2",
            required = true)
    @NotNull(message = "Количество попыток не может быть пустым")
    @Min(value = 1, message = "Количество попыток должно быть не меньше 1")
    @Max(value = 10, message = "Количество попыток не может превышать 10")
    private Integer maxAttempts;
}
