package co.com.sofka.questions.usecase;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

@Service
@Validated
public class CreateQuestionUseCase implements SaveQuestion {

    private final QuestionRepository repository;
    private final MapperUtils mapperUtils;

    public CreateQuestionUseCase(QuestionRepository repository, MapperUtils mapperUtils) {
        this.repository = repository;
        this.mapperUtils = mapperUtils;
    }

    @Override
    public Mono<QuestionDTO> apply(QuestionDTO questionDTO) {
        return repository.save(mapperUtils.mapperToQuestion(null).apply(questionDTO))
                .map(question -> mapperUtils.mapEntityToQuestion().apply(question));
    }
}
