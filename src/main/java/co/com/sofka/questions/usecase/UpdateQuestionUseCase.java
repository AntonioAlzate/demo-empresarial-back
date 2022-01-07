package co.com.sofka.questions.usecase;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class UpdateQuestionUseCase implements SaveQuestion{

    private final QuestionRepository questionRepository;
    private final MapperUtils mapperUtils;

    public UpdateQuestionUseCase(QuestionRepository questionRepository, MapperUtils mapperUtils) {
        this.questionRepository = questionRepository;
        this.mapperUtils = mapperUtils;
    }

    @Override
    public Mono<QuestionDTO> apply(QuestionDTO questionDTO) {
        String id = questionDTO.getId();
        Objects.requireNonNull(id, "Id of the question is required");
        return questionRepository
                .save(mapperUtils.mapperToQuestion(id).apply(questionDTO))
                .map(question -> mapperUtils.mapEntityToQuestion().apply(question));
    }
}
