package digital.ivan.lightchain.starter.pinecone;

import digital.ivan.lightchain.starter.pinecone.model.PineconeQuery;
import digital.ivan.lightchain.starter.pinecone.model.PineconeResponse;
import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class PineconeClientTest {

    private static ClientAndServer mockServer;

    @BeforeAll
    public static void startServer() {
        mockServer = startClientAndServer(1081);
    }

    @AfterAll
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void shouldGetResponseTest() {
        String expectedResponse = "{\n" +
                "    \"results\": [],\n" +
                "    \"matches\": [\n" +
                "        {\n" +
                "            \"id\": \"11741566\",\n" +
                "            \"score\": 0.832361221,\n" +
                "            \"values\": []\n" +
                "        }\n" +
                "    ],\n" +
                "    \"namespace\": \"\"\n" +
                "}";

        mockServer.when(
                        HttpRequest.request()
                                .withMethod("POST")
                                .withPath("/query"))
                .respond(
                        HttpResponse.response()
                                .withStatusCode(200)
                                .withBody(expectedResponse));

        PineconeClient pineconeClient = Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .target(PineconeClient.class, "http://localhost:1081");

        PineconeQuery query = new PineconeQuery(List.of(1d, 2d, 3d));

        PineconeResponse response = pineconeClient.query(query);

        assertNotNull(response);
        assertNotNull(response.getMatches());
    }
}
