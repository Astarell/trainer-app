package ru.mephi.trainer.service.attempt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import ru.mephi.trainer.entity.enums.TaskType;
import ru.mephi.trainer.models.attempt.answer.ErrorFindingAnswer;
import ru.mephi.trainer.models.attempt.answer.MultipleChoiceAnswer;
import ru.mephi.trainer.models.attempt.answer.OpenAnswer;
import ru.mephi.trainer.models.attempt.answer.SingleChoiceAnswer;
import ru.mephi.trainer.models.attempt.answer.UserAnswer;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
@RequiredArgsConstructor
public class UserAnswerParser {
    private final ObjectMapper objectMapper;

    public UserAnswer parse(String answerJson, TaskType taskType) {
        if (taskType == TaskType.OPEN_ANSWER || taskType == TaskType.ERROR_FINDING) {
            return parsePlainText(answerJson, taskType);
        }
        return parseJsonAnswer(answerJson, taskType);
    }

    private UserAnswer parsePlainText(String answer, TaskType taskType) {
        if (taskType == TaskType.OPEN_ANSWER) {
            return OpenAnswer.builder().answer(answer).build();
        } else if (taskType == TaskType.ERROR_FINDING) {
            return ErrorFindingAnswer.builder().answer(answer).build();
        }
        throw new IllegalArgumentException("Unexpected task type for plain text: " + taskType);
    }

    private UserAnswer parseJsonAnswer(String answerJson, TaskType taskType) {
        try {
            return switch (taskType) {
                case SINGLE_CHOICE -> parseSingleChoice(answerJson);
                case MULTIPLE_CHOICE -> parseMultipleChoice(answerJson);
                default -> throw new IllegalArgumentException("Unexpected task type for JSON: " + taskType);
            };
        } catch (NumberFormatException | JsonProcessingException e) {
            throw new IllegalArgumentException("Неверный формат ответа для типа задачи " + taskType, e);
        }
    }

    private SingleChoiceAnswer parseSingleChoice(String answer) throws NumberFormatException {
        int number = Integer.parseInt(answer);

        return SingleChoiceAnswer.builder()
                .selectedOrdinal(number)
                .build();
    }

    private MultipleChoiceAnswer parseMultipleChoice(String answerJson) throws JsonProcessingException {
        JsonNode node = objectMapper.readTree(answerJson);
        if (!node.isArray()) {
            throw new IllegalArgumentException("Для MULTIPLE_CHOICE ожидается массив");
        }

        Set<Integer> selectedOrdinals = new HashSet<>();
        for (JsonNode item : node) {
            selectedOrdinals.add(item.asInt());
        }

        return MultipleChoiceAnswer.builder()
                .selectedOrdinals(selectedOrdinals)
                .build();
    }
}
