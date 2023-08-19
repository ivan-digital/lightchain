package digital.ivan.lightchain.storage.s3;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.ivan.lightchain.core.context.model.Message;
import digital.ivan.lightchain.storage.exception.MessageParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@ConditionalOnProperty("lightchain.storage.s3.url")
public class S3MessageHistoryStorage {
    private static final String FOLDER = "history";
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private S3StorageAccessLayer s3StorageAccessLayer;


    public void saveMessage(Message message, String session) {
        try {
            s3StorageAccessLayer.uploadFile(
                    FOLDER + "/" + session + "_" + System.currentTimeMillis(),
                    mapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            throw new MessageParsingException(e);
        }
    }

    public List<Message> getMessages(String session) {
        return s3StorageAccessLayer.listFiles(FOLDER + "/" + session, true).stream()
                .map(key -> {
                    try {
                        return mapper.readValue(s3StorageAccessLayer.getFile(key), Message.class);
                    } catch (JsonProcessingException e) {
                        throw new MessageParsingException(e);
                    }
                })
                .collect(toList());
    }
}
