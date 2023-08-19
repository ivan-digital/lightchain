package digital.ivan.lightchain.starter.openai.model;


import com.fasterxml.jackson.annotation.JsonInclude;
import digital.ivan.lightchain.core.model.func.Function;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompletionRequest {
    public String getModel() {
        return model;
    }

    public double getTemperature() {
        return temperature;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Function> getFunctions() {
        return functions;
    }

    private String model;
    private double temperature;
    private List<Message> messages;
    private List<Function> functions;

    public CompletionRequest() {
    }

    public CompletionRequest(String model, double temperature, List<Message> messages, List<Function> functions) {
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
        this.functions = functions;
    }

    public CompletionRequest(String model, double temperature, List<Message> messages) {
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "CompletionRequest{" +
                "model='" + model + '\'' +
                ", temperature=" + temperature +
                ", messages=" + messages +
                ", functions=" + functions +
                '}';
    }
}

