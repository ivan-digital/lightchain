package digital.ivan.lightchain.core.context;

import digital.ivan.lightchain.core.context.model.SessionState;
import digital.ivan.lightchain.core.exception.SessionNotFoundException;

/**
 * The ISessionStateProvider interface is a contract for all session state provider implementations.
 * It is used to retrieve the state of a session.
 */
public interface ISessionStateProvider {

    /**
     * This method returns the session state based on the given session identifier.
     *
     * @param session - The identifier of the session whose state needs to be fetched.
     * @return The SessionState object representing the state of the session.
     * @throws IllegalArgumentException if the session identifier is null or empty.
     * @throws SessionNotFoundException if a session with the provided identifier does not exist.
     */
    SessionState getSessionState(String session);

    /**
     * Sets the state of a specific session.
     *
     * @param session The identifier of the session. This cannot be null.
     * @param state   The new state for the session. This cannot be null.
     * @throws IllegalArgumentException if either parameter is null.
     */
    void setSessionState(String session, SessionState state);
}
