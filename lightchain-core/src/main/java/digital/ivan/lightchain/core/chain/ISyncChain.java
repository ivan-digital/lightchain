package digital.ivan.lightchain.core.chain;

import digital.ivan.lightchain.core.model.LLMOutput;

import java.util.logging.Logger;

/**
 * Represents a synchronous processing chain, capable of transforming input data into a desired output.
 * The ISyncChain interface defines a contract for classes that perform synchronous data transformations.
 * Implementations of this interface should ensure data consistency and handle potential transformation errors.
 *
 * @param <Output> The type of the data after processing.
 * @param <Input> The type of the data before processing.
 */
public interface ISyncChain<Output, Input> {

    /**
     * Executes the synchronous data transformation operation using the provided input parameters.
     *
     * @param params The input data to be transformed.
     * @param logger An instance of a Logger to log relevant information or errors during the operation.
     * @return An instance of LLMOutput encapsulating the results of the transformation.
     */
    LLMOutput<Output> run(Input params, Logger logger);
}
