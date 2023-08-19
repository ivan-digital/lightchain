package digital.ivan.lightchain.core.router;

/**
 * Represents the contract for router tool calls.
 * Implementations of this interface should provide the means
 * to handle specific tool calls and provide a description of the tool.
 */
public interface IRouterToolCall {

    /**
     * Retrieves the RouterTool object associated with this tool call.
     * The RouterTool object provides details about the tool, such as its name and description.
     *
     * @return The RouterTool object associated with this tool call.
     */
    RouterTool getDescription();

    /**
     * Executes the tool call with the provided argument.
     *
     * @param argument The argument to be used in the tool call.
     * @return The result of the tool call as a string.
     */
    String call(String argument);
}
