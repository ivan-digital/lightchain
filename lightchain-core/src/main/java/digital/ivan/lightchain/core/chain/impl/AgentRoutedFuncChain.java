package digital.ivan.lightchain.core.chain.impl;

import digital.ivan.lightchain.core.chain.ISyncChain;
import digital.ivan.lightchain.core.chain.prompt.SystemMessagePropmtTemplate;
import digital.ivan.lightchain.core.context.model.Message;
import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.model.ILLModel;
import digital.ivan.lightchain.core.model.LLMInput;
import digital.ivan.lightchain.core.model.LLMOutput;
import digital.ivan.lightchain.core.model.func.LLMInputFunc;
import digital.ivan.lightchain.core.prompt.IPromptTemplateProvider;
import digital.ivan.lightchain.core.router.ToolCall;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a processing chain that handles agent routing functionality using a language learning model (LLM).
 * The AgentRoutedFuncChain is designed to generate a {@link ToolCall} based on a function-specific LLM input.
 *
 * <p>It follows the "chain of responsibility" pattern where it processes and generates output based on
 * the provided LLM and the input. This class builds upon the {@link SystemMessagePropmtTemplate} to provide
 * a system message context for the agent.
 *
 * <p>The primary interaction method is {@link #run(LLMInputFunc, Logger)}, which consumes a function-specific LLM input and
 * an optional logger. This method generates a {@link ToolCall} which indicates the tool and its associated action.
 *
 * <p>Errors and exceptions in the LLM output generation are wrapped into {@link OutputProcessingException} for clarity.
 *
 * @author Ivan Potapov
 * @see ISyncChain
 * @see ILLModel
 * @see SystemMessagePropmtTemplate
 * @see IPromptTemplateProvider
 */
public class AgentRoutedFuncChain implements ISyncChain<ToolCall, LLMInputFunc> {

    private final static String SYSTEM_ROLE = "system";
    private final ILLModel<ToolCall, LLMInputFunc> model;
    private final IPromptTemplateProvider promptTemplateProvider;

    public AgentRoutedFuncChain(ILLModel<ToolCall, LLMInputFunc> model) {
        this.promptTemplateProvider = new SystemMessagePropmtTemplate();
        this.model = model;
    }

    @Override
    public LLMOutput<ToolCall> run(LLMInputFunc input, Logger logger) {
        input.getContext().add(
                new Message(SYSTEM_ROLE,
                        promptTemplateProvider.getPrompt(),
                        System.currentTimeMillis()));
        var output = model.generate(new LLMInput<>(input)).stream()
                .max(Comparator.comparingDouble(LLMOutput::getProbability))
                .orElseThrow(() -> new OutputProcessingException("No result output of LLM"));
        if (logger != null)
            logger.log(Level.INFO, "For input: " + input.toString() + ", Output: " + output.getValue());
        return output;
    }
}
