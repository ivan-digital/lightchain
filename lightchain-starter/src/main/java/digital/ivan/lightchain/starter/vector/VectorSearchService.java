package digital.ivan.lightchain.starter.vector;

import digital.ivan.lightchain.core.model.EmbedModel;
import digital.ivan.lightchain.starter.pinecone.PineconeClient;
import digital.ivan.lightchain.starter.pinecone.model.PineconeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code VectorSearchService} class provides an implementation
 * of the {@link IVectorSearch} interface using the Pinecone client.
 * <p>
 * This service class generates embeddings for a given query string using
 * an {@code EmbedModel}, then queries Pinecone to retrieve matches based
 * on the embeddings. The results are then filtered based on a specified
 * threshold, returning only the matches that exceed this threshold.
 */
@Service
public class VectorSearchService implements IVectorSearch {

    @Autowired
    private PineconeClient pineconeClient;
    @Autowired
    private EmbedModel embedModel;
    @Value("${pinecone.threshold:0.7}")
    private double threshold;

    /**
     * Performs a vector search on the provided query string.
     * <p>
     * This method first generates embeddings for the input query using the
     * {@code EmbedModel}. It then queries Pinecone using these embeddings
     * and filters the matches based on the specified threshold.
     *
     * @param query The input string for which the vector search needs to be performed.
     * @return A list of matches that exceed the specified threshold.
     */
    @Override
    public List<String> search(String query) {
        var embeddings = embedModel.generate(query);
        return pineconeClient.query(new PineconeQuery(embeddings)).getMatches().stream()
                .filter(match -> match.getScore() > threshold)
                .map(match -> match.getMetadata().getInfo()).collect(Collectors.toList());
    }
}
