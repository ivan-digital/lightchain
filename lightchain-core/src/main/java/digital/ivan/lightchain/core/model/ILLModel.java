package digital.ivan.lightchain.core.model;

import java.util.List;

public interface ILLModel<Output, Input> {

    List<LLMOutput<Output>> generate(LLMInput<Input> input);
}