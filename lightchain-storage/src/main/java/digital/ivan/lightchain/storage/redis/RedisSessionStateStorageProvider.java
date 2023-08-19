package digital.ivan.lightchain.storage.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.ivan.lightchain.core.context.ISessionStateProvider;
import digital.ivan.lightchain.core.context.model.ChatState;
import digital.ivan.lightchain.core.context.model.SessionState;
import digital.ivan.lightchain.storage.exception.RedisParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@ConditionalOnProperty("spring.redis.host")
public class RedisSessionStateStorageProvider implements ISessionStateProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private StringRedisTemplate template;
    @Value("${redis.session.prefix:SESSION}")
    private String prefix;

    @Override
    public SessionState getSessionState(String session) {
        String state = this.template.opsForValue().get(prefix + ":" + session);
        if (state == null) {
            SessionState sessionState = new SessionState(ChatState.ACTIVE);
            this.setSessionState(session, sessionState);
            return sessionState;
        }
        try {
            return objectMapper.readValue(state, SessionState.class);
        } catch (IOException e) {
            throw new RedisParsingException(
                    "Failed to deserialize SessionState",
                    e);
        }
    }

    @Override
    public void setSessionState(String session, SessionState state) {
        String stateJson;
        try {
            stateJson = objectMapper.writeValueAsString(state);
        } catch (JsonProcessingException e) {
            throw new RedisParsingException(
                    "Failed to serialize SessionState: " + state, e);
        }
        this.template.opsForValue().set(session, stateJson);
    }
}
