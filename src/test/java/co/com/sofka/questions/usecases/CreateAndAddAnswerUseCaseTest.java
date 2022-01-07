package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Answer;
import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.usecase.CreateAndAddAnswerUseCase;
import co.com.sofka.questions.usecase.GetQuestionByIdUseCase;
import co.com.sofka.questions.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAndAddAnswerUseCaseTest {

    AnswerRepository answerRepository;
    GetQuestionByIdUseCase getQuestionByIdUseCase;
    CreateAndAddAnswerUseCase createAndAddAnswerUseCase;

    @BeforeEach
    public void setup() {
        MapperUtils mapperUtils = new MapperUtils();
        answerRepository = mock(AnswerRepository.class);
        getQuestionByIdUseCase = mock(GetQuestionByIdUseCase.class);
        createAndAddAnswerUseCase = new CreateAndAddAnswerUseCase(getQuestionByIdUseCase, answerRepository, mapperUtils);
    }

    @Test
    void createAndAddValidation() {

        var questionDTOP = new QuestionDTO("asd123", "asd", "¿Pregunta?", "OPEN", "SCIENCES");

        var answer = new Answer();
        answer.setId("123");
        answer.setUserId("asd");
        answer.setAnswer("answer");
        answer.setQuestionId("asd123");

        var answerDTO = new AnswerDTO("asd123", "asd", "answer");

        when(answerRepository.save(any())).thenReturn(Mono.just(answer));
        when(getQuestionByIdUseCase.apply("asd123")).thenReturn(Mono.just(questionDTOP));

        StepVerifier.create(createAndAddAnswerUseCase.apply(answerDTO))
                .expectNextMatches(questionDTO -> {
                    assert questionDTO.getId().equals("asd123");
                    assert questionDTO.getCategory().equals("SCIENCES");
                    assert questionDTO.getQuestion().equals("¿Pregunta?");
                    assert questionDTO.getType().equals("OPEN");
                    assert questionDTO.getAnswers().get(0).getAnswer().equals(answerDTO.getAnswer());
                    assert questionDTO.getAnswers().get(0).getUserId().equals(answerDTO.getUserId());
                    assert questionDTO.getAnswers().get(0).getQuestionId().equals(answerDTO.getQuestionId());
                    return true;
                })
                .verifyComplete();

        verify(answerRepository).save(any());
        verify(getQuestionByIdUseCase).apply("asd123");
    }
}