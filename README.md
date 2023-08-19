### LightChain - LLM Chat Assistant Tool

**The LightChain** is a **framework** for developing chat assistant applications.  At its core, 
LightChain provides essential libraries, interfaces, and tools to interact with language models 
and parse their outputs. Crafted to support Java 9, the framework simplifies tasks such as prompt 
engineering, content management, and routing of execution flows.

The framework's modular architecture includes **Spring Boot starter** and **storage** modules to 
enhance its capabilities..

#### Modules

* lightchain-core
* lightchain-starter
* lightchain-storage

#### Core library

#### Spring Boot Chat Starter

##### Getting started

To include LightChain in your project, simply add the following dependency:

```
<dependency>
    <groupId>digital.ivan.langchain</groupId>
    <artifactId>lightchain-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```

1. **Configuration:**
* For integration with OpenAI ChatGPT, set your API key in `openai.api-key`. 
* Define your LLM model in `openai.model.chat`.
* For embeddings using vector search, set the model name in `openai.model.embed`.
* If using Pinecone as a vector database for context storage, configure `pinecone.api-key` and `pinecone.url`.

2. **Tool Integration:**
* For out-of-the-box vector search and human interaction tools, include `@ComponentScan(basePackages = "digital.ivan.lightchain.starter.tools")` in your configuration.
* To extend functionality, implement the `IRouterToolCall` interface. For usage, a custom `IChatService` implementation is required. For reference, see the default routing agent class `ChatAgentRoutedWithHistoryService`.
3. **OpenAI Functionality:**
* To leverage OpenAI API functions directly within prompt templates, set the property `lightchain.agentrouted.func` to true in your `application.yml`.

#### Spring Boot Storage

Storage module implements interfaces from core library `IMessageHistoryProvider` (History of users conversation) on S3
and `ISessionStateProvider` (User sessions context) on Redis. In application property yaml expects the values for S3
compatible storage
configuration `lightchain.storage.s3.access_key`, `lightchain.storage.s3.secret_key`, `lightchain.storage.s3.url`, `lightchain.storage.s3.bucket_name`
and for redis `spring.redis.host` and `spring.redis.port`.

#### Custom prompt template example

To override default agent routed prompt template you need to implement `IPromptTemplateProvider` and create bean to
autowire `AgentRoutedChain`.

```agsl
@Configuration
public class AgentRoutedChainTemplateProvider implements IPromptTemplateProvider {
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
                "Question: {" + KeyConsts.INPUT + "}\n";

    }

    @Override
    public List<String> getRequiredPlaceholdersList() {
        return List.of(KeyConsts.TOOLS_DESCRIPTION.name(), KeyConsts.TOOLS_LIST.name(), KeyConsts.INPUT.name());
    }
}
```

```agsl
@Configuration
public class AgentRoutedChainChatConfig {
    @Bean
    AgentRoutedChain getAgentRoutedChain(OpenAiChatModel model,
                                         IRouterConfig routerConfig,
                                         IPromptTemplateProvider templateProvider) {
        IOutputParser parser = new SimpleAgentRoutedParser();
        return new AgentRoutedChain(model, parser, routerConfig, templateProvider);
    }
}
```

#### Custom parser

To change default behaviour of output parser you can implement `IOutputParser` interface.]

```agsl
@Configuration
public class AgentRoutedParser implements IOutputParser {
    public final static String THOUGHT = "Thought:";
    public final static String ACTION = "Action:";
    public final static String ACTION_INPUT = "Action Input:";

    @Override
    public ToolCall parse(String output) throws OutputProcessingException {
        if (!output.startsWith(THOUGHT) && output.indexOf(ACTION) == -1
                && output.indexOf(ACTION_INPUT) == -1) {
            throw new OutputProcessingException("Invalid output format. Output:" + output);
        }

        String outputWithoutAction = output.substring(
                output.indexOf(ACTION) + ACTION.length());
        String[] splitOutput = outputWithoutAction.split(ACTION_INPUT);

        if (splitOutput.length != 2) {
            throw new OutputProcessingException(
                    "Invalid output format. Expected action and action input. Output:" + output);
        }

        String toolName = splitOutput[0].trim();
        String[] parts = splitOutput[1].trim().split(":");
        String toolInput = parts[parts.length - 1];

        return new ToolCall(toolName, toolInput);
    }
}
```