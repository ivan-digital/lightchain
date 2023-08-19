package digital.ivan.lightchain.starter.vector;

import java.util.List;

/**
 * The IVectorSearch interface provides a mechanism for performing
 * vector-based search operations.
 * <p>
 * This interface allows implementations to search for relevant
 * vectors based on a given query, potentially utilizing
 * embedding techniques or other vector search methods.
 * <p>
 * Implementations of this interface could leverage databases
 * like Pinecone, Elasticsearch with vector plugins, or other
 * specialized vector search engines.
 */
public interface IVectorSearch {

    /**
     * Searches for vectors that are most relevant to the provided query.
     * <p>
     * @param query The search query, which could be a string or
     *              other form of input that can be converted to a vector.
     * @return A list of results that are most similar or relevant to the query.
     */
    List<String> search(String query);
}
