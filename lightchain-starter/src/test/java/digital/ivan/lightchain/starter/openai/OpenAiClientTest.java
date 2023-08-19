package digital.ivan.lightchain.starter.openai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import digital.ivan.lightchain.core.model.func.Function;
import digital.ivan.lightchain.starter.openai.model.*;
import digital.ivan.lightchain.starter.tools.HumanRouterTool;
import org.junit.jupiter.api.*;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class OpenAiClientTest {

    private static ClientAndServer mockServer;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(1082);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void shouldGetModelsTest() {
        String expectedResponse = "{ \"data\": [ { \"id\": \"gpt-3.5-turbo\" } ] }";

        mockServer.when(
                        HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/v1/models"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(expectedResponse));

        var webClient = WebClient.builder()
                .baseUrl("http://localhost:1082")
                .build();
        OpenAiClient client = new OpenAiClient(webClient, 1000);

        var response = client.getModels();

        assertNotNull(response);
        assertTrue(response.getData().size() > 0);

    }

    @Test
    public void shouldGetChatCompletions() throws JsonProcessingException {
        String expectedResponse = "{ \"id\": \"chatcmpl-123\", \"object\": \"chat.completion\" }";

        Message systemMessage = new Message("system", "You are a helpful assistant.");
        Message userMessage = new Message("user", "Hello!");

        var request = new CompletionRequest("gpt-3.5-turbo", 0.1, Arrays.asList(systemMessage, userMessage));

        mockServer.when(
                        HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/v1/chat/completions"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(expectedResponse));

        var webClient = WebClient.builder()
                .baseUrl("http://localhost:1082")
                .build();
        OpenAiClient client = new OpenAiClient(webClient, 1000);

        var response = client.chatCompletions(request);

        assertNotNull(response);
    }

    @Test
    public void shouldGetChatCompletionsWithFunc() throws JsonProcessingException {
        String expectedResponse = "{\n" +
                "  \"id\": \"chatcmpl-7p8r7z5VUwpLKYHZBpX6D4P9l7f1U\",\n" +
                "  \"object\": \"chat.completion\",\n" +
                "  \"created\": 1692423125,\n" +
                "  \"model\": \"gpt-3.5-turbo-0613\",\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"index\": 0,\n" +
                "      \"message\": {\n" +
                "        \"role\": \"assistant\",\n" +
                "        \"content\": \"Hello! How can I assist you today?\"\n" +
                "      },\n" +
                "      \"finish_reason\": \"stop\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"usage\": {\n" +
                "    \"prompt_tokens\": 122,\n" +
                "    \"completion_tokens\": 10,\n" +
                "    \"total_tokens\": 132\n" +
                "  }\n" +
                "}\n";

        Message systemMessage = new Message("system", "You are a helpful assistant.");
        Message userMessage = new Message("user", "Hello!");
        var tool = new HumanRouterTool();

        var request = new CompletionRequest("gpt-3.5-turbo",
                0.1,
                Arrays.asList(systemMessage, userMessage),
                List.of(new Function(
                        tool.getDescription().getName(),
                        tool.getDescription().getDescription(),
                        tool.getDescription().getFuncParams())));

        mockServer.when(
                        HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/v1/chat/completions"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(expectedResponse));

        var webClient = WebClient.builder()
                .baseUrl("http://localhost:1082")
                .build();
        OpenAiClient client = new OpenAiClient(webClient, 1000);

        var response = client.chatCompletions(request);

        assertNotNull(response);
    }
}
