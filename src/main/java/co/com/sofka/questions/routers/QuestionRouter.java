package co.com.sofka.questions.routers;

import co.com.sofka.questions.model.AnswerDTO;
import co.com.sofka.questions.model.QuestionDTO;
import co.com.sofka.questions.usecases.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class QuestionRouter {

    @Bean
    public RouterFunction<ServerResponse> createAndAddAnswer(CreateAndAddAnswerUseCase createAndAddAnswerUseCase) {
        return route(POST("/create/answer").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(AnswerDTO.class)
                        .flatMap(addAnswerDTO -> createAndAddAnswerUseCase.apply(addAnswerDTO)
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result))
                        )
        );
    }

    @Bean
    public RouterFunction<ServerResponse> createQuestion(CreateQuestionUseCase createQuestionUseCase) {
        return route(
                POST("/create/question").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(QuestionDTO.class).flatMap(questionDTO ->
                        createQuestionUseCase.apply(questionDTO)
                                .flatMap(result -> ServerResponse.ok()
                                        .contentType(MediaType.TEXT_PLAIN)
                                        .bodyValue(result))
                ));
    }

    @Bean
    public RouterFunction<ServerResponse> deleteQuestion(DeleteQuestionUseCase deleteQuestionUseCase) {
        return route(
                DELETE("/delete/question/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.accepted()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters
                                .fromPublisher(
                                        deleteQuestionUseCase.apply(request.pathVariable("id")),
                                        Void.class))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllQuestionsByOwner
            (GetAllQuestionsByOwnerUseCase getAllQuestionsByOwnerUseCase) {
        return route(
                GET("/get/questions/{userId}"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters
                                .fromPublisher(
                                        getAllQuestionsByOwnerUseCase.apply(request.pathVariable("userId")),
                                        QuestionDTO.class
                                ))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllQuestions(GetAllQuestionsUseCase getAllQuestionsUseCase) {
        return route(GET("/get/questions"),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters
                                .fromPublisher(
                                        getAllQuestionsUseCase.get(),
                                        QuestionDTO.class))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getQuestionById(GetQuestionByIdUseCase getQuestionByIdUseCase) {
        return route(
                GET("/get/question/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters
                                .fromPublisher(
                                        getQuestionByIdUseCase.apply(request.pathVariable("id")),
                                        QuestionDTO.class
                                ))
        );
    }

    @Bean
    public RouterFunction<ServerResponse> updateQuestion(UpdateQuestionUseCase updateQuestionUseCase) {
        return route(PUT("/question/{id}").and(accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToMono(QuestionDTO.class)
                        .flatMap(updateQuestionUseCase::apply)
                        .flatMap(result -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(result))
        );
    }
}
