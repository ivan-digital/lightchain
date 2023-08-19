package digital.ivan.lightchain.starter.chat;

import digital.ivan.lightchain.core.context.IMessageHistoryProvider;
import digital.ivan.lightchain.core.context.ISessionStateProvider;
import digital.ivan.lightchain.core.model.LLMOutput;
import digital.ivan.lightchain.starter.config.RouterConfig;
import digital.ivan.lightchain.starter.config.SimpleAgentRoutedChainChatConfig;
import digital.ivan.lightchain.starter.openai.OpenAiWebClientConfig;
import digital.ivan.lightchain.starter.tools.HumanRouterTool;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.logging.Logger;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        SimpleAgentRoutedChainChatConfig.class,
        OpenAiWebClientConfig.class,
        RouterConfig.class,
        HumanRouterTool.class,
        AgentRoutedWithHistoryService.class
})
class ChatAgentRoutedWithHistoryServiceTest {

    private final static String SESSION_ID = "session_1";

    @MockBean
    private AgentRoutedWithHistoryService agentRoutedWithHistoryService;
    @MockBean
    private IMessageHistoryProvider messageHistoryProvider;
    @MockBean
    private OpenAiChatModel openAiChatModel;
    @MockBean
    private ISessionStateProvider sessionStateProvider;
    @Mock
    private Logger logger;

    @Test
    void shouldProcessUserInputSync() {
        when(openAiChatModel.generate(any())).thenReturn(
                List.of(new LLMOutput<>(
                        "```{action:'NoNeed', action_input: 'attrs'}```", 1f)));
        agentRoutedWithHistoryService.sendUserInput("Hey", SESSION_ID, System.currentTimeMillis(), logger);
    }
}