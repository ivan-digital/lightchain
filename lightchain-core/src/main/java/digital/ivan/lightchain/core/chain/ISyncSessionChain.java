package digital.ivan.lightchain.core.chain;

import digital.ivan.lightchain.core.model.LLMOutput;

import java.util.Map;

/**
 * Represents a synchronous session-based chain for processing data.
 * The ISyncSessionChain interface defines a contract for classes that handle synchronous
 * data operations tied to a specific session. Implementations of this interface should ensure
 * data consistency and safety for session-based operations.
 *
 * @param <T> The type of the output data returned after processing.
 */
public interface ISyncSessionChain<T> {

    /**
     * Executes the synchronous operation using the provided parameters and session identifier.
     *
     * @param params A map of parameters required for the operation. The keys represent parameter names,
     *               and the corresponding values are the parameter values.
     * @param session The unique identifier representing the session associated with the operation.
     * @return An instance of LLMOutput containing the results of the operation.
     */
    LLMOutput<T> run(Map<String, String> params, String session);
}
