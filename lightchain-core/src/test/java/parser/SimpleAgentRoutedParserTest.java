package parser;

import digital.ivan.lightchain.core.exception.OutputProcessingException;
import digital.ivan.lightchain.core.parser.SimpleAgentRoutedParser;
import digital.ivan.lightchain.core.router.ToolCall;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleAgentRoutedParserTest {

    @Test
    void shouldParseCorrectly() throws OutputProcessingException {
        // Arrange
        SimpleAgentRoutedParser parser = new SimpleAgentRoutedParser();
        String output = "```\n" +
                "{\n" +
                "   \"action\": \"MyToolName\",\n" +
                "   \"action_input\": \"MyToolInput\"\n" +
                "}\n" +
                "```\n";
        ToolCall toolCall = parser.parse(output);

        assertEquals("MyToolName", toolCall.getToolName());
        assertEquals("MyToolInput", toolCall.getArgument());
    }

    @Test
    void shouldThrowExceptionOnInvalidInput() {
        SimpleAgentRoutedParser parser = new SimpleAgentRoutedParser();
        String invalidOutput = "invalid output";

        assertThrows(OutputProcessingException.class, () -> parser.parse(invalidOutput));
    }
}
