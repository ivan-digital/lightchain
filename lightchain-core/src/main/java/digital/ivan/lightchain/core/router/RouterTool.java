package digital.ivan.lightchain.core.router;

import java.util.Map;

/**
 * Represents a tool available within the routing framework.
 * <p>
 * This class encapsulates details about a particular tool, such as its name,
 * description, and any functional parameters it may require.
 */
public class RouterTool {

    /** The name of the tool. */
    private final String name;
    /** A brief description of the tool's purpose or functionality. */
    private final String description;
    /** The functional parameters associated with the tool. */
    private Map<String, Object> funcParams;

    /**
     * Constructs a RouterTool with the specified name and description.
     *
     * @param name The name of the tool.
     * @param description A brief description of the tool.
     */
    public RouterTool(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Constructs a RouterTool with the specified name, description, and functional parameters.
     *
     * @param name The name of the tool.
     * @param description A brief description of the tool.
     * @param funcParams A map representing the functional parameters for the tool.
     */
    public RouterTool(String name, String description, Map<String, Object> funcParams) {
        this.name = name;
        this.description = description;
        this.funcParams = funcParams;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getFuncParams() {
        return funcParams;
    }
}
