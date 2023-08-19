package digital.ivan.lightchain.starter.openai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CompletionResponse {
    private String id;
    private String object;
    private int created;
    private List<Choice> choices;
    private Usage usage;

    public CompletionResponse() {}
    public CompletionResponse(String id, String object, int created, List<Choice> choices, Usage usage) {
        this.id = id;
        this.object = object;
        this.created = created;
        this.choices = choices;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public int getCreated() {
        return created;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public Usage getUsage() {
        return usage;
    }

    public static class Choice {
        private int index;
        private MessageResponse message;
        @JsonProperty("finish_reason")
        private String finishReason;

        public Choice() {}
        public Choice(int index, MessageResponse message, String finishReason) {
            this.index = index;
            this.message = message;
            this.finishReason = finishReason;
        }

        public int getIndex() {
            return index;
        }

        public MessageResponse getMessage() {
            return message;
        }

        public String getFinishReason() {
            return finishReason;
        }
    }

    public static class Usage {
        @JsonProperty("prompt_tokens")
        private int promptTokens;
        @JsonProperty("completion_tokens")
        private int completionTokens;
        @JsonProperty("total_tokens")
        private int totalTokens;

        public Usage() {}
        public Usage(int promptTokens, int completionTokens, int totalTokens) {
            this.promptTokens = promptTokens;
            this.completionTokens = completionTokens;
            this.totalTokens = totalTokens;
        }

        public int getPromptTokens() {
            return promptTokens;
        }

        public int getCompletionTokens() {
            return completionTokens;
        }

        public int getTotalTokens() {
            return totalTokens;
        }
    }
}





