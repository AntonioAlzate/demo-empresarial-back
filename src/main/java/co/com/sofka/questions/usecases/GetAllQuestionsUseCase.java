package co.com.sofka.questions.usecases;

import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

import java.util.function.Supplier;

@Service
@Validated
public class GetAllQuestionsUseCase implements Supplier<Flux<QuestionDTO>> {

    private final QuestionRepository repository;
    private final MapperUtils mapperUtils;

    public GetAllQuestionsUseCase(QuestionRepository repository, MapperUtils mapperUtils) {
        this.repository = repository;
        this.mapperUtils = mapperUtils;
    }

    @Override
    public Flux<QuestionDTO> get() {
        return repository.findAll()
                .map(question -> mapperUtils.mapEntityToQuestion().apply(question));
    }
}
