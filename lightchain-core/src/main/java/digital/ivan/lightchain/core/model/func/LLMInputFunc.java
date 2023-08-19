package digital.ivan.lightchain.core.model.func;

import digital.ivan.lightchain.core.context.model.Message;
import digital.ivan.lightchain.core.router.IRouterToolCall;

import java.util.List;
import java.util.stream.Collectors;

public class LLMInputFunc {
    private final String input;
    private final List<Message> context;
    private final List<IRouterToolCall> tools;

    public LLMInputFunc(String input, List<Message> context, List<IRouterToolCall> tools) {
        this.input = input;
        this.context = context;
        this.tools = tools;
    }

    public String getInput() {
        return input;
    }

    public List<Message> getContext() {
        return context;
    }

    public List<Function> getTools() {
        return tools.stream()
                .map(tool -> new Function(
                        tool.getDescription().getName(),
                        tool.getDescription().getDescription(),
                        tool.getDescription().getFuncParams()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "LLMInputFunc{" +
                "input='" + input + '\'' +
                ", context=" + context +
                ", tools=" + tools +
                '}';
    }
}
