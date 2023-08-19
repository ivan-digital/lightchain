package digital.ivan.lightchain.starter.config;

import digital.ivan.lightchain.core.parser.IOutputParser;
import digital.ivan.lightchain.core.parser.SimpleAgentRoutedParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class responsible for defining the default {@link IOutputParser} bean.
 * <p>
 * This configuration is applied only if no other beans of type {@link IOutputParser} are present in the application
 * context. It provides an instance of the {@link SimpleAgentRoutedParser} as the default output parser.
 */
@Configuration
@ConditionalOnMissingBean(IOutputParser.class)
public class SimpleParserConfig {

    /**
     * Produces a default {@link IOutputParser} bean.
     *
     * @return An instance of {@link SimpleAgentRoutedParser}.
     */
    @Bean
    public IOutputParser simpleOutputParser() {
        return new SimpleAgentRoutedParser();
    }

}
