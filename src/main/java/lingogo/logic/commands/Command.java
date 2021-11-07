package lingogo.logic.commands;

import lingogo.logic.commands.exceptions.CommandException;
import lingogo.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Contains parameters that each command uses.
     */
    public enum Parameter {
        ENGLISH_PHRASE,
        FOREIGN_PHRASE,
        LANGUAGE,
        ENGLISH_KEYWORD,
        FOREIGN_KEYWORD,
        NUMBER_OF_FLASHCARDS,
        INDEX,
        INDEX_LIST,
        INDEX_RANGE,
        CSV_FILE_NAME;

        public String withCondition(String condition) {
            return toString() + " (" + condition + ")";
        }
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     *
     */
    public static String getMessageUsage(String commandWord, String commandDescription, String[] commandParameters,
                                  String[] commandExamples) {
        StringBuilder sb = new StringBuilder();
        sb.append(commandWord).append(": ");
        sb.append(commandDescription);
        sb.append("\n");
        sb.append("Parameters:");
        if (commandParameters.length == 0) {
            sb.append(" None");
        } else {
            for (String parameter : commandParameters) {
                sb.append(" ").append(parameter);
            }
        }
        sb.append("\n");
        sb.append("Examples: ");
        sb.append(commandExamples[0]);
        for (int i = 1; i < commandExamples.length; i++) {
            sb.append(" | ").append(commandExamples[i]);
        }
        return sb.toString();
    }
}
