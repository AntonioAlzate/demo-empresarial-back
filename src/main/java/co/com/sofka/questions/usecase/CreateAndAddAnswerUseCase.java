package co.com.sofka.questions.usecase;

import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.utils.MapperUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@Validated
public class CreateAndAddAnswerUseCase implements SaveAnswer {

    private final AnswerRepository answerRepository;
    private final MapperUtils mapperUtils;
    private final GetQuestionByIdUseCase getQuestionByIdUseCase;

    public CreateAndAddAnswerUseCase(GetQuestionByIdUseCase getQuestionByIdUseCase, AnswerRepository answerRepository, MapperUtils mapperUtils) {
        this.answerRepository = answerRepository;
        this.getQuestionByIdUseCase = getQuestionByIdUseCase;
        this.mapperUtils = mapperUtils;
    }

    @Override
    public Mono<QuestionDTO> apply(AnswerDTO answerDTO) {
        String idQuestion = answerDTO.getQuestionId();
        Objects.requireNonNull(idQuestion, "Id of the answer is required");
        return getQuestionByIdUseCase.apply(answerDTO.getQuestionId()).flatMap(question ->
                answerRepository.save(mapperUtils.mapperToAnswer().apply(answerDTO))
                        .map(answer -> {
                            question.getAnswers().add(answerDTO);
                            return question;
                        })
        );
    }
}
