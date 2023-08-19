package digital.ivan.lightchain.core.router;

/**
 * Represents a call to a specific tool with a given argument.
 * <p>
 * This class encapsulates information about a particular action or
 * function that needs to be performed, identified by its tool name and the
 * associated argument for that tool.
 */
public class ToolCall {

    /** The name of the tool to be invoked. */
    private final String toolName;
    /** The argument to be passed to the tool when invoked. */
    private final String argument;

    /**
     * Constructs a new ToolCall with the specified tool name and argument.
     *
     * @param toolName The name of the tool.
     * @param argument The argument to be passed to the tool.
     */
    public ToolCall(String toolName, String argument) {
        this.toolName = toolName;
        this.argument = argument;
    }

    /**
     * Retrieves the name of the tool.
     *
     * @return The name of the tool.
     */
    public String getToolName() {
        return toolName;
    }

    /**
     * Retrieves the argument associated with the tool call.
     *
     * @return The argument for the tool.
     */
    public String getArgument() {
        return argument;
    }

    /**
     * Provides a string representation of the ToolCall instance.
     *
     * @return A string representation detailing the tool name and its argument.
     */
    @Override
    public String toString() {
        return "ToolCall{" +
                "toolName='" + toolName + '\'' +
                ", argument='" + argument + '\'' +
                '}';
    }
}
