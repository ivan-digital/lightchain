package digital.ivan.lightchain.starter.openai.model;

import java.util.List;

public class EmbeddingResponse {
    private List<EmbeddingData> data;
    private String model;
    private String object;
    private Usage usage;

    public List<EmbeddingData> getData() {
        return data;
    }

    public static class EmbeddingData {
        private List<Double> embedding;
        private int index;
        private String object;

        public List<Double> getEmbedding() {
            return embedding;
        }
    }

    static class Usage {
        private int prompt_tokens;
        private int total_tokens;
    }
}
