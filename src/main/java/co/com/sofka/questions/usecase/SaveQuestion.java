package co.com.sofka.questions.usecase;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@FunctionalInterface
public interface SaveQuestion {
    Mono<QuestionDTO> apply(@Valid QuestionDTO questionDTO);
}
