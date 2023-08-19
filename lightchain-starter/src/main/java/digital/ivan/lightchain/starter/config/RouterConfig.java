package digital.ivan.lightchain.starter.config;

import digital.ivan.lightchain.core.exception.NoRouteAvailableException;
import digital.ivan.lightchain.core.router.IRouterConfig;
import digital.ivan.lightchain.core.router.IRouterToolCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

@Component
public class RouterConfig implements IRouterConfig {

    private Map<String, IRouterToolCall> toolsMap;
    private String description;
    private String tools;

    @Autowired
    void setTools(List<IRouterToolCall> tools) {
        this.toolsMap = tools.stream()
                .collect(toMap(tool -> tool.getDescription().getName(),
                        Function.identity()));
        this.description = tools.stream()
                .map(tool -> tool.getDescription().getName() + ":" + tool.getDescription().getDescription())
                .collect(joining("\n"));
        this.tools = tools.stream()
                .map(tool -> tool.getDescription().getName())
                .collect(joining(","));
    }

    @Override
    public IRouterToolCall mapRoute(String name) {
        if (!toolsMap.containsKey(name))
            throw new NoRouteAvailableException("No route for \"" + name + "\" is available.");
        return toolsMap.get(name);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStringToolsList() {
        return tools;
    }

    @Override
    public List<IRouterToolCall> getToolsList() {
        return new ArrayList<>(toolsMap.values());
    }
}
