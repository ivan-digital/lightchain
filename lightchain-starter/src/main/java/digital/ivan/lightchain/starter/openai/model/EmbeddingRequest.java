package digital.ivan.lightchain.starter.openai.model;

public class EmbeddingRequest {
    private String input;
    private String model;

    public EmbeddingRequest(String input, String model) {
        this.input = input;
        this.model = model;
    }

    public String getInput() {
        return input;
    }

    public String getModel() {
        return model;
    }
}
