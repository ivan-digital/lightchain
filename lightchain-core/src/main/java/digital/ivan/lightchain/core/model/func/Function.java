package digital.ivan.lightchain.core.model.func;

import java.util.Map;

public class Function {
    private String name;
    private String description;
    private Map<String, Object> parameters;

    public Function() {}

    public Function(String name, String description, Map<String, Object> parameters) {
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }
}