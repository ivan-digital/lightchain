package digital.ivan.lightchain.starter.chat;

import digital.ivan.lightchain.core.model.ILLModel;
import digital.ivan.lightchain.core.model.LLMInput;
import digital.ivan.lightchain.core.model.LLMOutput;
import digital.ivan.lightchain.starter.openai.OpenAiClient;
import digital.ivan.lightchain.starter.openai.model.CompletionRequest;
import digital.ivan.lightchain.starter.openai.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OpenAiChatModel implements ILLModel<String, String> {
    @Autowired
    private OpenAiClient openAiClient;
    @Value("${openai.model.chat}")
    private String model;
    @Value("${openai.temperature:0.1}")
    private double temperature;

    @Override
    public List<LLMOutput<String>> generate(LLMInput<String> input) {
        return openAiClient.chatCompletions(
                        new CompletionRequest(model, temperature,
                                List.of(new Message("user", input.getValue()))))
                .getChoices().stream()
                .map(choice ->
                        new LLMOutput<>(
                                choice.getMessage().getContent(),
                                choice.getIndex()))
                .collect(toList());
    }
}
