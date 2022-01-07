package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.usecase.GetAllQuestionsUseCase;
import co.com.sofka.questions.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetAllQuestionsUseCaseTest {

    QuestionRepository repository;
    GetAllQuestionsUseCase getAllQuestionsUseCase;

    @BeforeEach
    public void setup() {
        repository = mock(QuestionRepository.class);
        MapperUtils mapperUtils = new MapperUtils();
        getAllQuestionsUseCase = new GetAllQuestionsUseCase(repository, mapperUtils);
    }

    @Test
    void getAllQuestionsValidation() {
        var question = new Question();
        question.setUserId("asd123");
        question.setType("OPEN");
        question.setCategory("SCIENCES");
        question.setQuestion("¿Pregunta?");

        var result = Flux.just(question);

        when(repository.findAll()).thenReturn(result);

        StepVerifier.create(getAllQuestionsUseCase.get())
                .expectNextMatches(questionDTO -> {
                    assert questionDTO.getUserId().equals("asd123");
                    assert questionDTO.getCategory().equals("SCIENCES");
                    assert questionDTO.getQuestion().equals("¿Pregunta?");
                    assert questionDTO.getType().equals("OPEN");
                    return true;
                })
                .verifyComplete();

        verify(repository).findAll();
    }

}