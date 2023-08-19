package digital.ivan.lightchain.starter.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.ivan.lightchain.core.model.func.Function;
import digital.ivan.lightchain.starter.openai.model.CompletionRequest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumanRouterToolTest {

    @Test
    void shouldBeSerializable() throws JsonProcessingException {
        var tool = new HumanRouterTool();
        var request = new CompletionRequest("model",
                0.1, new ArrayList<>(),  List.of(new Function(
                        tool.getDescription().getName(),
                        tool.getDescription().getDescription(),
                        tool.getDescription().getFuncParams())));
        var mapper = new ObjectMapper();
        var json = mapper.writeValueAsString(request);
        assertNotNull(json);
    }

}