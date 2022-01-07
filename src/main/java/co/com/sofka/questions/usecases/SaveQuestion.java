package co.com.sofka.questions.usecases;

import co.com.sofka.questions.model.QuestionDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Service
@Validated
@FunctionalInterface
public interface SaveQuestion {
    Mono<QuestionDTO> apply(@Valid QuestionDTO questionDTO);
}
