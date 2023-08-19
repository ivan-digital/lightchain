package digital.ivan.lightchain.core.model;

public class LLMOutput<Value> {
    private final float probability;
    private final Value value;

    public LLMOutput(Value value, float probability) {
        this.value = value;
        this.probability = probability;
    }

    public LLMOutput(Value value) {
        this.value = value;
        this.probability = 1f;
    }

    public Value getValue() {
        return value;
    }

    public float getProbability() {
        return probability;
    }
}
