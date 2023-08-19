package digital.ivan.lightchain.starter.pinecone;

import feign.Feign;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for creating and configuring the {@link PineconeClient} instance.
 * <p>
 * This configuration provides a custom setup for the Feign client tailored for the Pinecone API, adding necessary
 * headers and using Gson for serialization and deserialization.
 */
@Configuration
public class PineconeFeignClientConfig {

    @Value("${pinecone.api-key}")
    private String apiKey;
    @Value("${pinecone.url}")
    private String url;

    /**
     * Produces a {@link PineconeClient} bean configured with Gson and a custom request interceptor for Pinecone API.
     * <p>
     * If a bean of type {@link PineconeClient} is already defined in the context, this bean definition will be ignored.
     * </p>
     *
     * @return Configured instance of {@link PineconeClient} tailored for the Pinecone API.
     */
    @Bean
    @ConditionalOnMissingBean
    public PineconeClient getFeignPinecone() {
        return Feign.builder()
                .decoder(new GsonDecoder())
                .encoder(new GsonEncoder())
                .requestInterceptor(template -> template.header(
                        "Api-Key",
                        apiKey))
                .target(PineconeClient.class, url);
    }
}
