package ru.mephi.trainer.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import ru.mephi.trainer.models.command.SaveTaskCommand;
import ru.mephi.trainer.models.taskconfig.AnswerChoice;
import ru.mephi.trainer.models.taskconfig.ErrorFindingConfig;
import ru.mephi.trainer.models.taskconfig.MultipleChoiceConfig;
import ru.mephi.trainer.models.taskconfig.OpenAnswerConfig;
import ru.mephi.trainer.models.taskconfig.SingleChoiceConfig;
import ru.mephi.trainer.rest.dto.request.task.AnswerChoiceDto;
import ru.mephi.trainer.rest.dto.request.task.ErrorFindingTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.MultipleChoiceTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.OpenAnswerTaskRequest;
import ru.mephi.trainer.rest.dto.request.task.SingleChoiceTaskRequest;

import java.util.List;

@ApplicationScoped
public class TaskMapper {

    // TODO MapStruct?

    public SaveTaskCommand toCommand(SingleChoiceTaskRequest request) {
        SingleChoiceConfig config = SingleChoiceConfig.builder()
                .question(request.getQuestion())
                .answerChoices(toAnswerChoices(request.getAnswerChoices()))
                .expectedOrdinal(request.getExpectedOrdinal())
                .points(request.getPoints())
                .mistakeCost(request.getMistakeCost())
                .maxAttempts(request.getMaxAttempts())
                .build();

        return SaveTaskCommand.builder()
                .trainerIds(request.getTrainerIds())
                .config(config)
                .build();
    }

    public SaveTaskCommand toCommand(MultipleChoiceTaskRequest request) {
        MultipleChoiceConfig config = MultipleChoiceConfig.builder()
                .question(request.getQuestion())
                .answerChoices(toAnswerChoices(request.getAnswerChoices()))
                .expectedOrdinals(request.getExpectedOrdinals())
                .points(request.getPoints())
                .mistakeCost(request.getMistakeCost())
                .maxAttempts(request.getMaxAttempts())
                .build();

        return SaveTaskCommand.builder()
                .trainerIds(request.getTrainerIds())
                .config(config)
                .build();
    }

    public SaveTaskCommand toCommand(ErrorFindingTaskRequest request) {
        ErrorFindingConfig config = ErrorFindingConfig.builder()
                .question(request.getQuestion())
                .context(request.getContext())
                .answerChoices(toAnswerChoices(request.getAnswerChoices()))
                .expectedOrdinals(request.getExpectedOrdinals())
                .points(request.getPoints())
                .mistakeCost(request.getMistakeCost())
                .maxAttempts(request.getMaxAttempts())
                .build();

        return SaveTaskCommand.builder()
                .trainerIds(request.getTrainerIds())
                .config(config)
                .build();
    }

    public SaveTaskCommand toCommand(OpenAnswerTaskRequest request) {
        OpenAnswerConfig config = OpenAnswerConfig.builder()
                .question(request.getQuestion())
                .context(request.getContext())
                .expectedAnswer(request.getExpectedAnswer())
                .points(request.getPoints())
                .mistakeCost(request.getMistakeCost())
                .maxAttempts(request.getMaxAttempts())
                .build();

        return SaveTaskCommand.builder()
                .trainerIds(request.getTrainerIds())
                .config(config)
                .build();
    }

    private AnswerChoice toAnswerChoice(AnswerChoiceDto dto) {
        if (dto == null) {
            return null;
        }
        return new AnswerChoice(dto.getOrdinal(), dto.getChoice());
    }

    private List<AnswerChoice> toAnswerChoices(List<AnswerChoiceDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return List.of();
        }
        return dtos.stream()
                .map(this::toAnswerChoice)
                .toList();
    }
}
