package digital.ivan.lightchain.starter.chat;

import digital.ivan.lightchain.core.model.ILLModel;
import digital.ivan.lightchain.core.model.LLMInput;
import digital.ivan.lightchain.core.model.LLMOutput;
import digital.ivan.lightchain.core.model.func.LLMInputFunc;
import digital.ivan.lightchain.core.router.ToolCall;
import digital.ivan.lightchain.starter.openai.OpenAiClient;
import digital.ivan.lightchain.starter.openai.model.CompletionRequest;
import digital.ivan.lightchain.starter.openai.model.Message;
import digital.ivan.lightchain.starter.openai.model.MessageResponse;
import digital.ivan.lightchain.starter.tools.HumanRouterTool;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class OpenAiChatFuncModel implements ILLModel<ToolCall, LLMInputFunc> {
    @Autowired
    private OpenAiClient openAiClient;
    @Autowired
    private HumanRouterTool humanRouterTool;
    @Value("${openai.model.chat}")
    private String model;
    @Value("${openai.temperature:0.1}")
    private double temperature;

    @Override
    public List<LLMOutput<ToolCall>> generate(LLMInput<LLMInputFunc> input) {
        var context = input.getValue().getContext().stream()
                .map(msg -> new Message(msg.getType(), msg.getValue()))
                .collect(toList());
        context.add(new Message("user", input.getValue().getInput()));
        var request = new CompletionRequest(model, temperature, context,
                input.getValue().getTools());
        return openAiClient.chatCompletions(request)
                .getChoices().stream()
                .map(choice ->
                        new LLMOutput<>(
                                processFunc(choice.getMessage()),
                                choice.getIndex()))
                .collect(toList());
    }

    private ToolCall processFunc(MessageResponse message) {
        if (message.getFunctionCall() != null) {
            var arguments = new JSONObject(message.getFunctionCall().getArguments());
            var argumentStr = arguments.keySet().stream()
                    .map(arguments::getString)
                    .collect(Collectors.joining(","));
            return new ToolCall(message.getFunctionCall().getName(), argumentStr);
        } else {
            return new ToolCall(humanRouterTool.getDescription().getName(),
                    message.getContent());
        }
    }
}
