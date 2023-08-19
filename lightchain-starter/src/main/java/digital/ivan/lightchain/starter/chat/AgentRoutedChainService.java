package digital.ivan.lightchain.starter.chat;

import digital.ivan.lightchain.core.chain.impl.AgentRoutedChain;
import digital.ivan.lightchain.core.chain.impl.AgentRoutedFuncChain;
import digital.ivan.lightchain.core.context.IMessageHistoryProvider;
import digital.ivan.lightchain.core.model.func.LLMInputFunc;
import digital.ivan.lightchain.core.router.IRouterConfig;
import digital.ivan.lightchain.core.router.ToolCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static digital.ivan.lightchain.core.prompt.KeyConsts.CONTEXT;
import static digital.ivan.lightchain.core.prompt.KeyConsts.INPUT;
import static digital.ivan.lightchain.starter.chat.AgentRoutedWithHistoryService.CHAT_HISTORY_LIMIT;

/**
 * This service handles the routing logic for user input based on configuration settings.
 * Depending on the configuration, it either routes the input through the {@link AgentRoutedChain} or
 * the {@link AgentRoutedFuncChain} to generate the appropriate tool call.
 * It also incorporates chat history into the context for the chat models.
 */
@Service
public class AgentRoutedChainService {

    @Value("${lightchain.agentrouted.func}")
    private Boolean funcRouter;
    @Autowired(required = false)
    private AgentRoutedChain agentRoutedChain;
    @Autowired(required = false)
    private AgentRoutedFuncChain agentRoutedFuncChain;
    @Autowired
    private IMessageHistoryProvider messageHistoryProvider;
    @Autowired
    private IRouterConfig routerConfig;

    /**
     * Processes the user's input and returns a tool call.
     * Depending on the configuration, the input is routed through either {@link AgentRoutedChain} or
     * {@link AgentRoutedFuncChain}. Chat history is used as context for the models.
     *
     * @param inputStr The user's input.
     * @param logger   Logger instance for logging operations.
     * @param session  The active chat session identifier.
     * @return A {@link ToolCall} containing the tool name and argument.
     */
    public ToolCall runRoutedChain(String inputStr, Logger logger, String session) {
        if (!funcRouter) {
            return agentRoutedChain.run(
                    new HashMap<>(Map.of(INPUT.name(), inputStr,
                            CONTEXT.name(), "Chat history: " + messageHistoryProvider.getLimitMessages(session, CHAT_HISTORY_LIMIT).stream()
                                    .map(msg -> msg.getType() + ":" + msg.getValue())
                                    .collect(Collectors.joining("\n")))),
                    logger).getValue();
        } else {
            var input = new LLMInputFunc(inputStr,
                    messageHistoryProvider.getLimitMessages(session, CHAT_HISTORY_LIMIT), routerConfig.getToolsList());
            return agentRoutedFuncChain.run(input, logger).getValue();
        }
    }

}
