package digital.ivan.lightchain.core.prompt;

/**
 * Enumeration representing a set of constants used as keys in the prompt template.
 */
public enum KeyConsts {
    CONTEXT,
    TOOLS_DESCRIPTION,
    TOOLS_LIST,
    INPUT;

    @Override
    public String toString() {
        return this.name();
    }
}
