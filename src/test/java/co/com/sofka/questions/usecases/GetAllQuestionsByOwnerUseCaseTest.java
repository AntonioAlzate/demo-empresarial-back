package co.com.sofka.questions.usecases;

import co.com.sofka.questions.collections.Question;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.utils.MapperUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class GetAllQuestionsByOwnerUseCaseTest {

    QuestionRepository questionRepository;
    GetAllQuestionsByOwnerUseCase getAllQuestionsByOwnerUseCase;

    @BeforeEach
    public void setup() {
        MapperUtils mapperUtils = new MapperUtils();
        questionRepository = mock(QuestionRepository.class);
        getAllQuestionsByOwnerUseCase = new GetAllQuestionsByOwnerUseCase(questionRepository, mapperUtils);
    }

    @Test
    void GetAllQuestionsByOwnerValidation() {
        var question1 = new Question();
        question1.setId("123");
        question1.setUserId("asd123");
        question1.setType("OPEN");
        question1.setCategory("SCIENCES");
        question1.setQuestion("¿Pregunta?");

        Flux<Question> questions = Flux.just(question1);

        when(questionRepository.findByUserId("asd123")).thenReturn(questions);

        StepVerifier.create(getAllQuestionsByOwnerUseCase.apply("asd123"))
                .expectNextMatches(questionDTO -> {
                    assert questionDTO.getUserId().equals("asd123");
                    assert questionDTO.getCategory().equals("SCIENCES");
                    assert questionDTO.getQuestion().equals("¿Pregunta?");
                    assert questionDTO.getType().equals("OPEN");
                    return true;
                })
                .verifyComplete();

        verify(questionRepository).findByUserId("asd123");
    }

}