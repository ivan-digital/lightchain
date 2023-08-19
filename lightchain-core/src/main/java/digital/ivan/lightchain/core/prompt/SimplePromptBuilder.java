package digital.ivan.lightchain.core.prompt;

import digital.ivan.lightchain.core.exception.InputParamsNotValidException;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

/**
 * The SimplePromptBuilder class is used for building prompts from templates provided by an IPromptTemplateProvider.
 * It decomposes the template into parts, each part representing either a static text or a placeholder.
 */
public class SimplePromptBuilder {

    private List<Function<Map<String, String>, String>> parts;
    private IPromptTemplateProvider templateProvider;

    /**
     * Public constructor to create a new instance of SimplePromptBuilder.
     *
     * @param templateProvider the provider of the prompt template.
     * @throws IllegalArgumentException if duplicate keys are found in the template.
     */
    public SimplePromptBuilder(IPromptTemplateProvider templateProvider) {
        this.templateProvider = templateProvider;
        parts = new ArrayList<>();
        Matcher m = Pattern.compile("\\{(.*?)}|([^{}]+)").matcher(templateProvider.getPrompt());
        Set<String> keySet = new HashSet<>();
        while (m.find()) {
            if (m.group(1) != null) {
                String key = m.group(1);
                if (!keySet.add(key)) {
                    throw new IllegalArgumentException("Duplicate key found in template: " + key);
                }
                parts.add(keys -> retrieveFromKeys(keys, key));
            } else {
                String str = m.group(2);
                parts.add(keys -> str);
            }
        }
    }

    /**
     * Builds a prompt using the provided key-value pairs to replace the placeholders in the template.
     *
     * @param keys a Map of placeholders where keys are the placeholder names and values are the actual values.
     * @return the built prompt as a String.
     * @throws InputParamsNotValidException if the keys map doesn't contain all required placeholders.
     */
    public String build(Map<String, String> keys) throws InputParamsNotValidException {
        templateProvider.validate(keys);
        String result = parts.stream().map(part -> part.apply(keys)).collect(joining());
        return result;
    }

    /**
     * Retrieves a value from a Map using a provided key. If the key is not found in the Map,
     * an IllegalArgumentException is thrown.
     *
     * @param keys a Map of keys to values
     * @param key  the key to retrieve a value for
     * @return the value associated with the key from the Map
     * @throws IllegalArgumentException if the key is not found in the Map
     */
    private String retrieveFromKeys(Map<String, String> keys, String key) {
        if (keys.containsKey(key)) {
            return keys.get(key);
        } else {
            throw new IllegalArgumentException("Required key missing: " + key);
        }
    }
}
