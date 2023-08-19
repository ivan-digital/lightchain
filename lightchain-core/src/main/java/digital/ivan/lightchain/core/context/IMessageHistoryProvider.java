package digital.ivan.lightchain.core.context;

import digital.ivan.lightchain.core.context.model.Message;

import java.util.List;

/**
 * The IMessageHistoryProvider interface defines the contract for classes that manage the history of a chat session.
 * It provides methods to save messages into a session's history, retrieve the history of a session.
 */
public interface IMessageHistoryProvider {

    /**
     * Saves a message into the history of a specified chat session.
     *
     * @param message the Message to be saved into the history
     * @param session the identifier of the chat session to which the history belongs
     */
    void saveMessage(Message message, String session);

    /**
     * Retrieves all messages associated with a given chat session.
     *
     * @param session The unique identifier representing the chat session.
     * @return A list of chat messages associated with the session.
     */
    List<Message> getMessages(String session);

    /**
     * Retrieves a limited number of recent messages associated with a given chat session.
     * This can be useful for scenarios where only the most recent chat history is needed.
     *
     * @param session The unique identifier representing the chat session.
     * @param limit The maximum number of recent messages to be retrieved.
     * @return A list containing up to {@code limit} recent messages from the chat session.
     */
    List<Message> getLimitMessages(String session, int limit);
}
