package digital.ivan.lightchain.core.chain.impl;

import digital.ivan.lightchain.core.chain.ISyncChain;
import digital.ivan.lightchain.core.chain.prompt.AgentRoutedPromptTemplateProvider;
import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.model.ILLModel;
import digital.ivan.lightchain.core.model.LLMInput;
import digital.ivan.lightchain.core.model.LLMOutput;
import digital.ivan.lightchain.core.parser.IOutputParser;
import digital.ivan.lightchain.core.prompt.IPromptTemplateProvider;
import digital.ivan.lightchain.core.prompt.KeyConsts;
import digital.ivan.lightchain.core.prompt.SimplePromptBuilder;
import digital.ivan.lightchain.core.router.IRouterConfig;
import digital.ivan.lightchain.core.router.ToolCall;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a processing chain for routing agents based on the interaction with a language learning model (LLM).
 * The AgentRoutedChain class handles the process of generating {@link ToolCall} outputs based on map inputs.
 *
 * <p>Two primary constructors allow for configuration flexibility. The first constructor requires an additional
 * {@link IPromptTemplateProvider} parameter, whereas the second one defaults to using
 * {@link AgentRoutedPromptTemplateProvider}.
 *
 * <p>The processing workflow within this chain consists of:
 * <ol>
 *   <li>Preparing parameters, particularly the tools description and tools list.</li>
 *   <li>Calling the LLM model to get a raw output based on the parameters.</li>
 *   <li>Parsing the raw output using the provided {@link IOutputParser} to generate a {@link ToolCall} output.</li>
 * </ol>
 *
 * <p>Logging is integrated at various points in the processing chain to provide insights and traceability.
 *
 * @author Ivan Potapov
 * @see ISyncChain
 * @see ILLModel
 * @see IOutputParser
 * @see IPromptTemplateProvider
 * @see IRouterConfig
 * @see SimplePromptBuilder
 * @see AgentRoutedPromptTemplateProvider
 */
public class AgentRoutedChain implements ISyncChain<ToolCall, Map<String, String>> {

    private final ILLModel<String, String> model;
    private final SimplePromptBuilder promptBuilder;
    private final String toolsDescription;
    private final String toolsList;
    private final IOutputParser parser;

    public AgentRoutedChain(ILLModel<String, String> model,
                            IOutputParser parser,
                            IRouterConfig properties,
                            IPromptTemplateProvider templateProvider) {
        this.model = model;
        this.parser = parser;
        this.promptBuilder = new SimplePromptBuilder(templateProvider);
        this.toolsDescription = properties.getDescription();
        this.toolsList = properties.getStringToolsList();
    }

    public AgentRoutedChain(ILLModel<String, String> model,
                            IOutputParser parser,
                            IRouterConfig properties) {
        this.model = model;
        this.parser = parser;
        this.promptBuilder = new SimplePromptBuilder(new AgentRoutedPromptTemplateProvider());
        this.toolsDescription = properties.getDescription();
        this.toolsList = properties.getStringToolsList();
    }

    @Override
    public LLMOutput<ToolCall> run(Map<String, String> params, Logger logger) throws OutputProcessingException {
        params.put(KeyConsts.TOOLS_DESCRIPTION.name(), this.toolsDescription);
        params.put(KeyConsts.TOOLS_LIST.name(), this.toolsList);
        return new LLMOutput<>(parser.parse(callModel(params, logger)));
    }

    private String callModel(final Map<String, String> params, Logger logger) {
        Map<String, String> localParams = new HashMap<>(params);
        LLMInput<String> input = new LLMInput<>(promptBuilder.build(localParams));
        Optional<LLMOutput<String>> optional = this.model.generate(input).stream()
                .max(Comparator.comparingDouble(LLMOutput::getProbability));
        LLMOutput<String> output = optional.orElseThrow(() -> new OutputProcessingException("No result output of LLM"));
        if (logger != null)
            logger.log(Level.INFO, "For input: " + input.getValue() + ", Output: " + output.getValue());
        return output.getValue();
    }
}
