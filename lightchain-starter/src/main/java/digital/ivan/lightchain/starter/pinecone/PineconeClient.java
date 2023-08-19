package digital.ivan.lightchain.starter.pinecone;

import digital.ivan.lightchain.starter.pinecone.model.PineconeQuery;
import digital.ivan.lightchain.starter.pinecone.model.PineconeResponse;
import feign.Headers;
import feign.RequestLine;

public interface PineconeClient {
    @RequestLine("POST /query")
    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"})
    PineconeResponse query(PineconeQuery query);
}
