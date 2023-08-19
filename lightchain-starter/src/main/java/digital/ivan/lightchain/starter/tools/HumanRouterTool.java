package digital.ivan.lightchain.starter.tools;

import digital.ivan.lightchain.core.router.IRouterToolCall;
import digital.ivan.lightchain.core.router.RouterTool;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

/**
 * The {@code HumanRouterTool} class provides an implementation of the
 * {@link IRouterToolCall} interface to facilitate human intervention in
 * situations where the machine does not have sufficient information.
 * <p>
 * This tool allows for user queries to be directly returned as responses,
 * essentially acting as a pass-through mechanism for user-directed questions.
 */
@Component
public class HumanRouterTool implements IRouterToolCall {

    /**
     * Retrieves the description and metadata for this human router tool.
     * <p>
     * The tool essentially facilitates the user to directly interact with the human,
     * ensuring that any query can be directly passed for human intervention.
     *
     * @return A {@link RouterTool} object containing the tool's name, description,
     *         and expected input properties.
     */
    @Override
    public RouterTool getDescription() {
        var questionProperty = new HashMap<>();
        questionProperty.put("type", "string");
        questionProperty.put("description", "Question you need to ask the human.");


        var properties = new HashMap<>();
        properties.put("question", questionProperty);

        var root = new HashMap<String, Object>();
        root.put("type", "object");
        root.put("properties", properties);
        root.put("required", List.of("question"));

        return new RouterTool(
                "Human",
                "If you need any additional information from human to process the input you can use this tool and ask short and clear question.",
                root);
    }

    /**
     * Passes the provided argument directly as a response.
     * <p>
     * Given this tool's purpose of facilitating direct human interaction, it
     * simply returns the argument, acting as a passthrough.
     *
     * @param argument The query string to be relayed for human interaction.
     * @return The same argument string, unchanged.
     */
    @Override
    public String call(String argument) {
        return argument;
    }
}
