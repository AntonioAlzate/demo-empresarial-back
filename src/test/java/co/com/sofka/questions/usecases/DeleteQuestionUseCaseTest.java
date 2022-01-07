package co.com.sofka.questions.usecases;

import co.com.sofka.questions.reposioties.AnswerRepository;
import co.com.sofka.questions.reposioties.QuestionRepository;
import co.com.sofka.questions.usecase.DeleteQuestionUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

class DeleteQuestionUseCaseTest {

    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    DeleteQuestionUseCase deleteQuestionUseCase;

    @BeforeEach
    public void setup(){
        questionRepository = mock(QuestionRepository.class);
        answerRepository = mock(AnswerRepository.class);
        deleteQuestionUseCase = new DeleteQuestionUseCase(questionRepository, answerRepository);
    }

    @Test
    void deleteQuestionValidation(){
        String id = "123";
        when(questionRepository.deleteById(id)).thenReturn(Mono.empty());
        when(answerRepository.deleteByQuestionId(id)).thenReturn(Mono.empty());

        StepVerifier.create(deleteQuestionUseCase.apply(id))
                .verifyComplete();

        verify(questionRepository).deleteById(id);
        verify(answerRepository).deleteByQuestionId(id);
    }

}