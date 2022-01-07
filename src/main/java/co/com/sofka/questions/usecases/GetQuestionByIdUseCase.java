package co.com.sofka.questions.usecases;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

@Service
@Validated
public class GetQuestionByIdUseCase implements Function<String, Mono<QuestionDTO>> {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final MapperUtils mapperUtils;

    public GetQuestionByIdUseCase(QuestionRepository questionRepository, AnswerRepository answerRepository, MapperUtils mapperUtils) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.mapperUtils = mapperUtils;
    }

    @Override
    public Mono<QuestionDTO> apply(String id) {
        Objects.requireNonNull(id, "el id es requerido");

        return questionRepository.findById(id)
                .map(question -> mapperUtils.mapEntityToQuestion().apply(question))
                .flatMap(questionDTO -> {
                    return Mono.just(questionDTO).zipWith(
                            answerRepository.findAllByQuestionId(id)
                                    .map(answer -> mapperUtils.mapEntityToAnswer().apply(answer))
                                    .collectList(),
                            (question, answers) -> {
                                question.setAnswers(answers);
                                return question;
                            }
                    );
                });
    }
}
