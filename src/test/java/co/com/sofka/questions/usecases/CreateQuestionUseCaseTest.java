package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.usecase.CreateQuestionUseCase;
import co.com.sofka.questions.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateQuestionUseCaseTest {

    QuestionRepository repository;
    CreateQuestionUseCase createQuestionUseCase;

    @BeforeEach
    public void setup(){
        MapperUtils mapperUtils = new MapperUtils();
        repository = mock(QuestionRepository.class);
        createQuestionUseCase = new CreateQuestionUseCase(repository, mapperUtils);
    }

    @Test
    void createQuestionValidation(){
        var question =  new Question();
        question.setUserId("asd123");
        question.setType("OPEN");
        question.setCategory("SCIENCES");
        question.setQuestion("¿Pregunta?");

        var questionDTOP = new QuestionDTO("asd123", "¿Pregunta?", "OPEN", "SCIENCES");
        when(repository.save(any())).thenReturn(Mono.just(question));

        StepVerifier.create(createQuestionUseCase.apply(questionDTOP))
                .expectNextMatches(questionDTO -> {
                    assert questionDTO.getUserId().equals("asd123");
                    assert questionDTO.getCategory().equals("SCIENCES");
                    assert questionDTO.getQuestion().equals("¿Pregunta?");
                    assert questionDTO.getType().equals("OPEN");
                    return true;
                })
                .verifyComplete();

        verify(repository).save(any());
    }

}