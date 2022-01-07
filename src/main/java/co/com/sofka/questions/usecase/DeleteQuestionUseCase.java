package co.com.sofka.questions.usecase;

import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

public class DeleteQuestionUseCase implements Function<String, Mono<Void>> {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public DeleteQuestionUseCase(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public Mono<Void> apply(String id) {
        Objects.requireNonNull(id, "El id es requerido");
        return questionRepository.deleteById(id)
                .switchIfEmpty(Mono.defer(() -> answerRepository.deleteByQuestionId(id)));
    }
}
