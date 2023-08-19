package digital.ivan.lightchain.core.parser;

import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.router.ToolCall;

/**
 * Represents a simple parser for agent-routed outputs.
 * <p>
 * This parser specifically focuses on outputs with prefixed labels such as "Thought:", "Action:", and "Action Input:"
 * to derive a {@link ToolCall} object representing the tool name and its input.
 */
public class SimpleAgentRoutedParser implements IOutputParser {
    public final static String THOUGHT = "Thought:";
    public final static String ACTION = "Action:";
    public final static String ACTION_INPUT = "Action Input:";

    /**
     * Parses the provided output string to extract a {@link ToolCall} object.
     * <p>
     * The output string should have prefixed sections identified by the labels "Thought:", "Action:", and
     * "Action Input:". Any deviation from this format will result in an {@link OutputProcessingException}.
     *
     * @param output The output string to be parsed.
     * @return A {@link ToolCall} object derived from the parsed output.
     * @throws OutputProcessingException if the output format is invalid or unexpected.
     */
    @Override
    public ToolCall parse(String output) throws OutputProcessingException {
        if (!output.startsWith(THOUGHT) && output.indexOf(ACTION) != 1
                && output.indexOf(ACTION_INPUT) != 1) {
            throw new OutputProcessingException("Invalid output format. Output:" + output);
        }

        String outputWithoutAction = output.substring(
                output.indexOf(ACTION) + ACTION.length());
        String[] splitOutput = outputWithoutAction.split(ACTION_INPUT);

        if (splitOutput.length != 2) {
            throw new OutputProcessingException(
                    "Invalid output format. Expected action and action input. Output:" + output);
        }

        String toolName = splitOutput[0].trim();
        String toolInput = splitOutput[1].trim();

        return new ToolCall(toolName, toolInput);
    }
}

