package digital.ivan.lightchain.starter.openai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class OpenAiWebClientConfig {

    @Value("${openai.api-key}")
    private String apiKey;

    @Bean
    public WebClient openAiWebClient(@Autowired WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.openai.com")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }
}
