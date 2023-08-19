package digital.ivan.lightchain.starter.config;


import digital.ivan.lightchain.core.chain.impl.AgentRoutedChain;
import digital.ivan.lightchain.core.chain.impl.AgentRoutedFuncChain;
import digital.ivan.lightchain.core.parser.IOutputParser;
import digital.ivan.lightchain.core.router.IRouterConfig;
import digital.ivan.lightchain.starter.chat.OpenAiChatFuncModel;
import digital.ivan.lightchain.starter.chat.OpenAiChatModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for defining the chat routing agent chains.
 * <p>
 * This class provides bean definitions for both {@link AgentRoutedChain} and
 * {@link AgentRoutedFuncChain} based on the value of the 'lightchain.agentrouted.func' property.
 * <p>
 * When 'lightchain.agentrouted.func' property is set to 'false', an instance of
 * {@link AgentRoutedChain} will be configured. If it's set to 'true', an instance of
 * {@link AgentRoutedFuncChain} will be provided instead.
 */
@Configuration
public class SimpleAgentRoutedChainChatConfig {

    /**
     * Provides a bean definition for {@link AgentRoutedChain}.
     *
     * @param model        Instance of {@link OpenAiChatModel}.
     * @param routerConfig Instance of {@link IRouterConfig}.
     * @param parser       Instance of {@link IOutputParser}.
     * @return An initialized instance of {@link AgentRoutedChain}.
     */
    @Bean
    @ConditionalOnProperty(value = "lightchain.agentrouted.func", havingValue = "false")
    @ConditionalOnMissingBean(AgentRoutedChain.class)
    AgentRoutedChain getAgentRoutedChain(OpenAiChatModel model,
                                         IRouterConfig routerConfig,
                                         IOutputParser parser) {
        return new AgentRoutedChain(model, parser, routerConfig);
    }

    /**
     * Provides a bean definition for {@link AgentRoutedFuncChain}.
     *
     * @param model Instance of {@link OpenAiChatFuncModel}.
     * @return An initialized instance of {@link AgentRoutedFuncChain}.
     */
    @Bean
    @ConditionalOnProperty(value = "lightchain.agentrouted.func", havingValue = "true")
    @ConditionalOnMissingBean(AgentRoutedFuncChain.class)
    AgentRoutedFuncChain getAgentRoutedFuncChain(OpenAiChatFuncModel model) {
        return new AgentRoutedFuncChain(model);
    }
}
