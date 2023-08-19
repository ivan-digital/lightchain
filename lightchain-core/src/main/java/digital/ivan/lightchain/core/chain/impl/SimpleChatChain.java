package digital.ivan.lightchain.core.chain.impl;

import digital.ivan.lightchain.core.chain.ISyncSessionChain;
import digital.ivan.lightchain.core.chain.prompt.ChatWithContextPromptTemplateProvider;
import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.model.ILLModel;
import digital.ivan.lightchain.core.model.LLMInput;
import digital.ivan.lightchain.core.model.LLMOutput;
import digital.ivan.lightchain.core.prompt.SimplePromptBuilder;

import java.util.Comparator;
import java.util.Map;

/**
 * Represents a chat processing chain, primarily designed to work with language learning models (LLMs).
 * The SimpleChatChain class provides the capability to generate chat responses using the provided LLM.
 * It builds the chat prompts with the assistance of {@link SimplePromptBuilder}.
 *
 * <p>The class incorporates the "chain of responsibility" design pattern, whereby it forms a segment of a
 * larger chain to process a series of actions in a chat session. The primary interaction method is {@link #run(Map, String)}
 * which takes input parameters and a session identifier to generate a chat response.
 *
 * <p>Any exceptions in the LLM output generation are captured and wrapped into {@link OutputProcessingException}
 * to provide a clear indication of the failure.
 *
 * @see ISyncSessionChain
 * @see ILLModel
 * @see SimplePromptBuilder
 * @see ChatWithContextPromptTemplateProvider
 */
public class SimpleChatChain implements ISyncSessionChain<String> {

    private final ILLModel<String, String> model;
    private final SimplePromptBuilder promptBuilder;

    public SimpleChatChain(ILLModel<String, String> model) {
        this.model = model;
        this.promptBuilder = new SimplePromptBuilder(new ChatWithContextPromptTemplateProvider());
    }

    @Override
    public LLMOutput<String> run(Map<String, String> params, String session) throws OutputProcessingException {
        LLMInput<String> input = new LLMInput<>(promptBuilder.build(params));
        return this.model.generate(input).stream()
                .max(Comparator.comparingDouble(LLMOutput::getProbability))
                .orElseThrow(() -> new OutputProcessingException("No result output of LLM"));
    }
}
