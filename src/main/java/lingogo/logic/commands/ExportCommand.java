package lingogo.logic.commands;

import static java.util.Objects.requireNonNull;
import static lingogo.commons.util.FileUtil.exportToCsvFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lingogo.commons.core.Messages;
import lingogo.logic.commands.exceptions.CommandException;
import lingogo.model.Model;
import lingogo.model.flashcard.Flashcard;

/**
 * Exports the existing cards in the flashcard app to a CSV file.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";
    public static final String COMMAND_DESCRIPTION =
            "Exports flashcards to a CSV file where your data folder is located.";
    public static final String[] COMMAND_PARAMETERS = new String[] {
        Parameter.CSV_FILE_NAME.withCondition("must have a valid file name with .csv extension.")
    };
    public static final String[] COMMAND_EXAMPLES = new String[] {
        COMMAND_WORD + " myCards.csv"
    };

    public static final String MESSAGE_USAGE =
            getMessageUsage(COMMAND_WORD, COMMAND_DESCRIPTION, COMMAND_PARAMETERS, COMMAND_EXAMPLES);

    public static final String MESSAGE_SUCCESS =
            "The current displayed list of flashcards have been saved in %1$s (located in the data folder).";

    public static final String EXPORT_IOEXCEPTION = "Could not save flashcards from LingoGO! into %1$s";

    private static final String[] csvHeaders = {"Language", "Foreign", "English"};
    private final String fileName;



    /**
     * @param fileName of the CSV file to be exported from the flashcard app
     */
    public ExportCommand(String fileName) {
        requireNonNull(fileName);

        this.fileName = fileName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.isSlideshowActive()) {
            throw new CommandException(Messages.MESSAGE_IN_SLIDESHOW_MODE);
        }

        File dataFolder = new File("data");
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        List<String[]> content = flashcardListToCsvFormat(model.getFilteredFlashcardList());

        try {
            exportToCsvFile("data/" + fileName, csvHeaders, content);
        } catch (IOException ioe) {
            throw new CommandException(String.format(EXPORT_IOEXCEPTION, fileName));
        }

        return new CommandResult(String.format(MESSAGE_SUCCESS, fileName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExportCommand // instanceof handles nulls
                && fileName.equals(((ExportCommand) other).fileName)); // state check
    }

    private List<String[]> flashcardListToCsvFormat(List<Flashcard> flashcards) {
        return flashcards.stream()
                .map(e -> new String[] {e.getLanguageType().value,
                        e.getForeignPhrase().value, e.getEnglishPhrase().value})
                .collect(Collectors.toList());
    }
}
