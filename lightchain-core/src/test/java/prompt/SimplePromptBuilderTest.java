package prompt;

import digital.ivan.lightchain.core.prompt.IPromptTemplateProvider;
import digital.ivan.lightchain.core.prompt.SimplePromptBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimplePromptBuilderTest {

    @Test
    void shouldTemplateCorrectly() {
        IPromptTemplateProvider templateProvider = new IPromptTemplateProvider() {
            @Override
            public String getPrompt() {
                return "Hello {name}, {message}";
            }

            @Override
            public List<String> getRequiredPlaceholdersList() {
                return List.of("name", "message");
            }
        };
        SimplePromptBuilder template = new SimplePromptBuilder(templateProvider);
        Map<String, String> params = Map.of("name", "Ivan", "message", "hi");
        assertEquals(template.build(params), "Hello Ivan, hi");
    }

    @Test
    void shouldThrowsException() {
        IPromptTemplateProvider templateProvider = new IPromptTemplateProvider() {
            @Override
            public String getPrompt() {
                return "Hello {name}, {name}, {message}\"";
            }

            @Override
            public List<String> getRequiredPlaceholdersList() {
                return null;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> {
            new SimplePromptBuilder(templateProvider);
        }, "Duplicate key found in template: name");
    }

}