package digital.ivan.lightchain.storage.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.ivan.lightchain.core.context.IMessageHistoryProvider;
import digital.ivan.lightchain.core.context.model.Message;
import digital.ivan.lightchain.storage.exception.RedisParsingException;
import digital.ivan.lightchain.storage.s3.S3MessageHistoryStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Service
@ConditionalOnProperty({"spring.redis.host", "lightchain.storage.s3.url"})
public class RedisMessageStorageProvider implements IMessageHistoryProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RedisSessionStateStorageProvider redisSessionStateStorageProvider;
    @Autowired
    private S3MessageHistoryStorage s3MessageHistoryStorage;
    @Value("${redis.history.prefix:HISTORY}")
    private String prefix;
    @Value("${redis.history.ttl:1}")
    private long ttl;
    @Autowired
    private StringRedisTemplate template;

    @Override
    @Async
    public void saveMessage(Message message, String session) {
        try {
            template.opsForList().leftPush(getKey(session),
                    objectMapper.writeValueAsString(message));
            template.expire(getKey(session), Duration.ofHours(ttl));
            s3MessageHistoryStorage.saveMessage(message, session);
        } catch (JsonProcessingException e) {
            throw new RedisParsingException(
                    "Failed to serialize Message: " + message, e);
        }
    }

    @Override
    public List<Message> getMessages(String session) {
        return getLimitMessages(session, -1);
    }

    @Override
    public List<Message> getLimitMessages(String session, int limit) {
        var result = new ArrayList<Message>();
        for (var message : requireNonNull(
                template.opsForList().range(
                        getKey(session),
                        0, limit))) {
            try {
                result.add(objectMapper.readValue(message, Message.class));
            } catch (JsonProcessingException e) {
                throw new RedisParsingException(
                        "Failed to serialize Message: " + message, e);
            }
        }
        return result;
    }

    private String getKey(String session) {
        return prefix + ":" + session;
    }

}
