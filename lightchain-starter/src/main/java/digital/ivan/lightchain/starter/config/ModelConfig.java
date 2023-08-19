package digital.ivan.lightchain.starter.config;

import digital.ivan.lightchain.core.chain.impl.SimpleChatChain;
import digital.ivan.lightchain.starter.chat.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {
    @Bean
    SimpleChatChain getChatHistoryChain(OpenAiChatModel model) {
        return new SimpleChatChain(model);
    }

}
