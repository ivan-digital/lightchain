package digital.ivan.lightchain.core.chain.prompt;

import digital.ivan.lightchain.core.parser.SimpleAgentRoutedParser;
import digital.ivan.lightchain.core.prompt.IPromptTemplateProvider;
import digital.ivan.lightchain.core.prompt.KeyConsts;

import java.util.List;

public class AgentRoutedPromptTemplateProvider implements IPromptTemplateProvider {
    @Override
    public String getPrompt() {
        return "Respond to the human as helpfully and accurately as possible. " +
                "You have access to the following tools:\n" +
                "{" + KeyConsts.TOOLS_DESCRIPTION + "}\n\n" +
                "Use the following format to suggest the action with tool:\n" +
                "Question: the input question you must answer\n" +
                SimpleAgentRoutedParser.THOUGHT + " you should always think about what to do\n" +
                SimpleAgentRoutedParser.ACTION + " the action to take, should be one of [{" + KeyConsts.TOOLS_LIST + "}]\n" +
                SimpleAgentRoutedParser.ACTION_INPUT + " the input to the action\n" +
                "Begin! Response to the human as helpfully and accurately as possible.\n\n" +
                "{" + KeyConsts.CONTEXT.name() + "}\n\n" +
                "Question: {" + KeyConsts.INPUT + "}\n";
    }

    @Override
    public List<String> getRequiredPlaceholdersList() {
        return List.of(KeyConsts.TOOLS_DESCRIPTION.name(), KeyConsts.TOOLS_LIST.name(), KeyConsts.INPUT.name());
    }
}
