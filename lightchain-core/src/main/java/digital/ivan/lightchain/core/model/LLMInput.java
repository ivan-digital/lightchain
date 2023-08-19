package digital.ivan.lightchain.core.model;


public class LLMInput<Value> {
    private final Value value;

    public LLMInput(Value value) {
        this.value = value;
    }

    public Value getValue() {
        return value;
    }
}
