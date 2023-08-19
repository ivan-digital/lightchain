package digital.ivan.lightchain.core.parser;

import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.router.ToolCall;

/**
 * The IOutputParser is a functional interface that defines a method for parsing output from the Large Language Model (LLM).
 * This interface is designed to allow lambda expressions or method references for parsing the output into a ToolCall object.
 */
@FunctionalInterface
public interface IOutputParser {

    /**
     * Parses the output from the LLM into a ToolCall object.
     *
     * @param output the output from the LLM as a String.
     * @return a ToolCall object resulting from the parsing of the output.
     * @throws OutputProcessingException if an error occurs during the output processing.
     */
    ToolCall parse(String output) throws OutputProcessingException;
}
