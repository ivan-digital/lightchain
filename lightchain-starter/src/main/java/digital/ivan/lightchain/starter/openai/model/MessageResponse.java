package digital.ivan.lightchain.starter.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageResponse {
    private String role;
    private String content;
    @JsonProperty("function_call")
    private FunctionCall functionCall;

    public MessageResponse() {}

    public MessageResponse(String role, String content, FunctionCall functionCall) {
        this.role = role;
        this.content = content;
        this.functionCall = functionCall;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public FunctionCall getFunctionCall() {
        return functionCall;
    }

    public static class FunctionCall {
        public FunctionCall() {}

        private String name;
        private String arguments;

        public String getName() {
            return name;
        }

        public String getArguments() {
            return arguments;
        }
    }
}
