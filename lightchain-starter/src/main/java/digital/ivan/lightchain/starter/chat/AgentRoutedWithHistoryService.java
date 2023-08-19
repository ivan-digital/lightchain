package digital.ivan.lightchain.starter.chat;

import digital.ivan.lightchain.core.chain.impl.SimpleChatChain;
import digital.ivan.lightchain.core.context.IMessageHistoryProvider;
import digital.ivan.lightchain.core.context.ISessionStateProvider;
import digital.ivan.lightchain.core.context.model.ChatState;
import digital.ivan.lightchain.core.context.model.Message;
import digital.ivan.lightchain.core.context.model.SessionState;
import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.router.IRouterConfig;
import digital.ivan.lightchain.core.router.IRouterToolCall;
import digital.ivan.lightchain.core.router.ToolCall;
import digital.ivan.lightchain.starter.tools.HumanRouterTool;
import digital.ivan.lightchain.starter.tools.VectorSearchTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static digital.ivan.lightchain.core.context.model.ChatState.ACTIVE;
import static digital.ivan.lightchain.core.context.model.ChatState.WAITING_FOR_HUMAN;
import static digital.ivan.lightchain.core.prompt.KeyConsts.CONTEXT;
import static digital.ivan.lightchain.core.prompt.KeyConsts.INPUT;

/**
 * Implementation of the {@link IChatService} to handle chat interactions while retaining chat history.
 * <p>
 * This service facilitates user and assistant interactions by routing messages using configured tools,
 * preserving a limited chat history, and handling different chat states, including awaiting human response.
 * When a user sends input, the service can route it to various tools (like HumanRouterTool or VectorSearchTool),
 * and the response is based on the tool's operation.
 * <p>
 * The chat history is maintained for a limited number of messages, and this history, combined with potential
 * additional context (e.g., search results), is provided to the chat model for contextual awareness.
 */
@Service
@ConditionalOnMissingBean(IChatService.class)
public class AgentRoutedWithHistoryService implements IChatService {

    public final static int CHAT_HISTORY_LIMIT = 4;
    private final static String ASSISTANT = "assistant";
    private final static String USER = "user";

    @Autowired
    private AgentRoutedChainService routedChain;
    @Autowired
    private SimpleChatChain simpleChatChain;
    @Autowired
    private IRouterConfig routerConfig;
    @Autowired
    private IMessageHistoryProvider messageHistoryProvider;
    @Autowired
    private ISessionStateProvider sessionStateProvider;

    /**
     * Asynchronously processes the user's input, updating the chat state, preserving the message history,
     * and routing through the appropriate tools.
     *
     * @param input   The user's input message.
     * @param session The active chat session identifier.
     * @param utc     The timestamp for the message in UTC.
     * @param logger  Logger instance for logging operations.
     */
    @Async
    @Override
    public void sendUserInput(String input, String session, long utc, Logger logger) {
        SessionState state = sessionStateProvider.getSessionState(session);
        messageHistoryProvider.saveMessage(new Message(USER, input, utc), session);

        if (state.getChatState() == WAITING_FOR_HUMAN) {
            updateChatStateAndSaveMessage(ACTIVE, session, state,
                    new Message(ASSISTANT, runChatChain(input, fetchChatContext(session, null), session),
                            System.currentTimeMillis()));
        } else {
            ToolCall call = routedChain.runRoutedChain(input, logger, session);
            IRouterToolCall tool = routerConfig.mapRoute(call.getToolName());
            if (tool instanceof HumanRouterTool) {
                updateChatStateAndSaveMessage(
                        WAITING_FOR_HUMAN,
                        session,
                        state,
                        new Message(ASSISTANT, call.getArgument(), System.currentTimeMillis()));
            } else if (tool instanceof VectorSearchTool) {
                updateChatStateAndSaveMessage(ACTIVE, session, state,
                        new Message(ASSISTANT, runChatChain(input, fetchChatContext(session, tool.call(call.getArgument())), session),
                                System.currentTimeMillis()));
            } else {
                throw new OutputProcessingException("Returned tool is not supported. Name: "
                        + call.getToolName() + ", argument: " + call.getArgument());
            }
        }
    }

    /**
     * Updates the chat state for the given session and saves the provided message.
     *
     * @param chatState The new chat state to be set.
     * @param session   The active chat session identifier.
     * @param state     The current session state.
     * @param message   The message to be saved.
     */
    private void updateChatStateAndSaveMessage(ChatState chatState, String session, SessionState state, Message message) {
        state.setChatState(chatState);
        sessionStateProvider.setSessionState(session, state);
        messageHistoryProvider.saveMessage(message, session);
    }

    /**
     * Retrieves the chat context based on the chat history and an optional additional context.
     *
     * @param session The active chat session identifier.
     * @param context Optional additional context, can be null.
     * @return A string representing the chat context.
     */
    private String fetchChatContext(String session, String context) {
        var result = "Chat history: " + messageHistoryProvider.getLimitMessages(session, CHAT_HISTORY_LIMIT).stream()
                .map(msg -> msg.getType() + ":" + msg.getValue())
                .collect(Collectors.joining("\n"));
        if (context != null)
            result += "\nSearch results:\n"
                    + context;
        return result;
    }

    /**
     * Executes the chat chain model to process the given input with the provided context.
     *
     * @param input   The user's input.
     * @param context The chat context.
     * @param session The active chat session identifier.
     * @return The chat model's response.
     */
    private String runChatChain(String input, String context, String session) {
        return simpleChatChain.run(new HashMap<>(Map.of(INPUT.name(), input, CONTEXT.name(), context)), session).getValue();
    }
}