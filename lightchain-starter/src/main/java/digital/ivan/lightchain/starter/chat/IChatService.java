package digital.ivan.lightchain.starter.chat;

import java.util.logging.Logger;

/**
 * Represents the primary interface for chat services.
 * <p>
 * This interface provides a method to send user inputs to a chat service
 * along with associated session data and logger. Implementing classes
 * are expected to handle and process the user input accordingly. To not
 * use logger functionality is expected to pass null logger object.
 */
public interface IChatService {

    /**
     * Sends user input to the chat service for processing.
     *
     * @param input    The user input/message to be sent to the chat service.
     * @param session  The session identifier to associate the input with a specific session.
     * @param utc      The Coordinated Universal Time (UTC) at which the message was sent.
     * @param logger   The logger instance to log any relevant information or errors.
     */
    void sendUserInput(String input, String session, long utc, Logger logger);
}
