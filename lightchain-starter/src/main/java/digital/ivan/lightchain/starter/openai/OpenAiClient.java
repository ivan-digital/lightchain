package digital.ivan.lightchain.starter.openai;

import digital.ivan.lightchain.starter.exeption.EnrichedWebClientResponseException;
import digital.ivan.lightchain.starter.openai.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Service
public class OpenAiClient {

    public OpenAiClient(@Autowired WebClient openAiWebClient,
                        @Value("${openai.client.timeout:10000}") int timeout) {
        this.openAiWebClient = openAiWebClient;
        this.timeout = timeout;
    }

    private final WebClient openAiWebClient;
    private final int timeout;

    public ModelsList getModels() {
        return openAiWebClient.get()
                .uri("/v1/models")
                .retrieve()
                .bodyToMono(ModelsList.class)
                .block(Duration.ofMillis(timeout));
    }

    public CompletionResponse chatCompletions(CompletionRequest body) {
        return openAiWebClient.post()
                .uri("/v1/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(CompletionResponse.class)
                .onErrorMap(WebClientResponseException.class,
                        e -> new EnrichedWebClientResponseException(e.getMessage() +
                                ", HTTP code: " + e.getRawStatusCode() + ", Body: " + e.getResponseBodyAsString()))
                .block(Duration.ofMillis(timeout));
    }

    public EmbeddingResponse getEmbedding(EmbeddingRequest body) {
        return openAiWebClient.post()
                .uri("/v1/embeddings")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(EmbeddingResponse.class)
                .onErrorMap(WebClientResponseException.class,
                        e -> new EnrichedWebClientResponseException(e.getMessage() +
                                ", HTTP code: " + e.getRawStatusCode() + ", Body: " + e.getResponseBodyAsString()))
                .block(Duration.ofMillis(timeout));
    }
}
