package digital.ivan.lightchain.core.prompt;

import digital.ivan.lightchain.core.exception.InputParamsNotValidException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The IPromptTemplateProvider interface defines methods that allow a prompt template to be provided,
 * including a list of required placeholders, and a validation method for these placeholders.
 */
public interface IPromptTemplateProvider {

    /**
     * Returns the prompt for the Large Language Model (LLM).
     *
     * @return the prompt as a String.
     */
    String getPrompt();

    /**
     * Returns a list of required placeholders for the LLM prompt.
     *
     * @return a List of required placeholders as String objects.
     */
    List<String> getRequiredPlaceholdersList();

    /**
     * Validates if all required placeholders exist in the provided map.
     * If not, it throws an InputParamsNotValidException.
     *
     * @param map a Map of placeholders where keys are the placeholder names and values are the actual values.
     * @throws InputParamsNotValidException if the map doesn't contain all required placeholders.
     */
    default void validate(Map<String, String> map) throws InputParamsNotValidException {
        if (!getRequiredPlaceholdersList().stream()
                .allMatch(map::containsKey))
            throw new InputParamsNotValidException("Template params are missing: " + getRequiredPlaceholdersList().stream().filter(item -> !map.containsKey(item)).collect(Collectors.joining(",")));
    }
}
