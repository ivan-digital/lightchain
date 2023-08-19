package digital.ivan.lightchain.starter.vector;

import digital.ivan.lightchain.core.model.EmbedModel;
import digital.ivan.lightchain.starter.openai.OpenAiClient;
import digital.ivan.lightchain.starter.openai.model.EmbeddingRequest;
import digital.ivan.lightchain.starter.openai.model.EmbeddingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The {@code OpenAiEmbedModel} service provides functionality for
 * generating embeddings using the OpenAI client.
 * <p>
 * This implementation of {@link EmbedModel} uses the specified OpenAI embedding model
 * to convert a given input string into its corresponding embedding representation.
 */
@Service
public class OpenAiEmbedModel implements EmbedModel {

    @Autowired
    private OpenAiClient openAiClient;
    @Value("${openai.model.embed}")
    private String model;

    /**
     * Generates an embedding representation for the given input string.
     * <p>
     * This method calls the OpenAI service to obtain an embedding for the specified input.
     * The embedding is then returned as a list of double values.
     *
     * @param input The input string for which the embedding needs to be generated.
     * @return A list of double values representing the embedding.
     * @throws IllegalArgumentException if the input string is {@code null}.
     * @throws RuntimeException if no embedding data is available for the input.
     */
    @Override
    public List<Double> generate(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null.");
        }

        var embeddingResponse = openAiClient.getEmbedding(new EmbeddingRequest(input, model));

        return embeddingResponse.getData().stream()
                .findFirst()
                .map(EmbeddingResponse.EmbeddingData::getEmbedding)
                .orElseThrow(() -> new RuntimeException("No embedding data available for the given input."));

    }
}
