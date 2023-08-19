package digital.ivan.lightchain.starter.config;

import digital.ivan.lightchain.starter.chat.OpenAiChatModel;
import digital.ivan.lightchain.starter.openai.OpenAiWebClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {
        SimpleAgentRoutedChainChatConfig.class,
        OpenAiWebClientConfig.class,
        OpenAiChatModel.class,
        RouterConfig.class})
class SimpleAgentRoutedChainTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void shouldRouteChat() {
        SimpleAgentRoutedChainChatConfig service =
                context.getBean(SimpleAgentRoutedChainChatConfig.class);
        assertNotNull(service);
    }

}