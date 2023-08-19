package digital.ivan.lightchain.starter.pinecone.model;

import java.util.List;

public class PineconeQuery {
    private List<Double> vector;
    private int topK;
    private boolean includeMetadata;
    private String namespace;

    public PineconeQuery(List<Double> vector) {
        this.vector = vector;
        this.topK = 3;
        this.includeMetadata = true;
        this.namespace = "";
    }

    public PineconeQuery(List<Double> vector, int topK) {
        this.vector = vector;
        this.topK = topK;
        this.includeMetadata = true;
        this.namespace = "";
    }
}
