package co.com.sofka.questions.usecase;

import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Service
@Validated
@FunctionalInterface
public interface SaveAnswer {
    Mono<QuestionDTO> apply(@Valid AnswerDTO answerDTO);
}
