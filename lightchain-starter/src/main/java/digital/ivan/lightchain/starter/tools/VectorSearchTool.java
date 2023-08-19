package digital.ivan.lightchain.starter.tools;

import digital.ivan.lightchain.core.router.IRouterToolCall;
import digital.ivan.lightchain.core.router.RouterTool;
import digital.ivan.lightchain.starter.vector.VectorSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * This {@code VectorSearchTool} class provides an implementation of the
 * {@link IRouterToolCall} interface, enabling vector-based searching capabilities.
 * <p>
 * Utilizing the {@link VectorSearchService}, this tool allows querying
 * and returning search results based on vector embeddings.
 * The class also provides descriptive metadata about the tool itself,
 * defining expected properties and types.
 */
@Component
public class VectorSearchTool implements IRouterToolCall {
    @Autowired
    private VectorSearchService vectorSearchService;

    /**
     * Retrieves the description and metadata for this vector search tool.
     *
     * @return A {@link RouterTool} object containing the tool's name, description,
     *         and expected input properties.
     */
    @Override
    public RouterTool getDescription() {
        var queryProperty = new HashMap<>();
        queryProperty.put("type", "string");
        queryProperty.put("description", "Query you want to search.");

        var properties = new HashMap<>();
        properties.put("query", queryProperty);

        var root = new HashMap<String, Object>();
        root.put("type", "object");
        root.put("properties", properties);
        root.put("required", List.of("query"));

        return new RouterTool(
                "Search",
                "Use this tool when you need to search for additional information.",
                root);
    }

    /**
     * Calls the vector search tool with the provided argument.
     * <p>
     * This method leverages the {@link VectorSearchService} to perform a vector-based search
     * on the provided argument and returns the concatenated results as a string.
     *
     * @param argument The query string for the vector search.
     * @return A concatenated string of the search results.
     */
    @Override
    public String call(String argument) {
        return String.join("\n", vectorSearchService.search(argument));
    }
}
