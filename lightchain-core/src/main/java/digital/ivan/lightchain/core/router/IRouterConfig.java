package digital.ivan.lightchain.core.router;

import java.util.List;

/**
 * Represents the contract for router configurations.
 * This interface provides methods for mapping routes to their respective tools,
 * retrieving tool descriptions, and listing available tools.
 */
public interface IRouterConfig {

    /**
     * Maps a given route name to its corresponding tool call.
     *
     * @param name The name of the route to be mapped.
     * @return The IRouterToolCall instance associated with the given route name.
     */
    IRouterToolCall mapRoute(String name);

    /**
     * Retrieves a description of the router configuration.
     *
     * @return A string representation of the router's description.
     */
    String getDescription();

    /**
     * Retrieves a string representation of the list of available tools.
     *
     * @return A string listing all the available tools.
     */
    String getStringToolsList();

    /**
     * Retrieves the list of all available tool calls.
     *
     * @return A list of IRouterToolCall instances representing all available tools.
     */
    List<IRouterToolCall> getToolsList();
}
