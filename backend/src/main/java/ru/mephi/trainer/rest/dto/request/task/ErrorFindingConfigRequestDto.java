package ru.mephi.trainer.rest.dto.request.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import ru.mephi.trainer.entity.enums.TaskType;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "ErrorFindingConfigRequestDto", description = "Конфигурация задания на поиск ошибок")
public class ErrorFindingConfigRequestDto implements TaskConfigRequestDto {

    @Schema(description = "Текст вопроса",
            examples = "Найдите недостатки или риски валидации.")
    private String question;

    @Schema(description = "Контекст задания (описание ситуации, код, диаграмма)",
            examples = "Для поля ИНН организации в форме партнёрства заданы правила: только цифры, не может начинаться с 0...")
    private String context;

    @Schema(description = "Список вариантов ответов (возможные ошибки)")
    private List<AnswerChoiceDto> answerChoices;

    @Schema(description = "Список порядковых номеров правильных ответов (начиная с 1)",
            examples = "[1, 2, 4]")
    private List<Integer> expectedOrdinals;

    @Schema(description = "Максимальное количество баллов за задание",
            examples = "20",
            minimum = "1",
            maximum = "100")
    private Integer points;

    @Schema(description = "Штраф за каждый неправильно выбранный вариант (в баллах)",
            examples = "3",
            minimum = "0",
            maximum = "100")
    private Integer mistakeCost;

    @Schema(description = "Максимальное количество попыток выполнения задания",
            examples = "2",
            minimum = "1",
            maximum = "10")
    private Integer maxAttempts;

    @Override
    public TaskType getTaskType() {
        return TaskType.ERROR_FINDING;
    }
}
