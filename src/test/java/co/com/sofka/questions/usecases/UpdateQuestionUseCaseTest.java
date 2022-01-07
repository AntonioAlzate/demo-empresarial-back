package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateQuestionUseCaseTest {

    QuestionRepository questionRepository;
    UpdateQuestionUseCase updateQuestionUseCase;

    @BeforeEach
    public void setup() {
        MapperUtils mapperUtils = new MapperUtils();
        questionRepository = mock(QuestionRepository.class);
        updateQuestionUseCase = new UpdateQuestionUseCase(questionRepository, mapperUtils);
    }

    @Test
    void updateQuestionValidation() {
        var question = new Question();
        question.setId("asd123");
        question.setUserId("123");
        question.setType("OPEN");
        question.setCategory("SCIENCES");
        question.setQuestion("¿Pregunta?");

        var questionDTOP = new QuestionDTO("asd123", "123", "¿Pregunta?", "OPEN", "SCIENCES");
        when(questionRepository.save(any())).thenReturn(Mono.just(question));

        StepVerifier.create(updateQuestionUseCase.apply(questionDTOP))
                .expectNextMatches(questionDTO -> {
                    assert questionDTO.getId().equals("asd123");
                    assert questionDTO.getUserId().equals("123");
                    assert questionDTO.getCategory().equals("SCIENCES");
                    assert questionDTO.getQuestion().equals("¿Pregunta?");
                    assert questionDTO.getType().equals("OPEN");
                    return true;
                })
                .verifyComplete();

        verify(questionRepository).save(any());
    }

}