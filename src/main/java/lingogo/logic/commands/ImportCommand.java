package lingogo.logic.commands;

import static java.util.Objects.requireNonNull;
import static lingogo.commons.core.Messages.MESSAGE_FILE_NOT_FOUND;
import static lingogo.commons.core.Messages.MESSAGE_INVALID_CSV_CONTENT;
import static lingogo.commons.core.Messages.MESSAGE_INVALID_CSV_HEADERS;
import static lingogo.commons.util.FileUtil.importCsvFileContent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.exceptions.CsvValidationException;

import lingogo.commons.core.Messages;
import lingogo.commons.exceptions.CsvColumnHeaderException;
import lingogo.commons.exceptions.CsvNumColumnsException;
import lingogo.logic.commands.exceptions.CommandException;
import lingogo.model.Model;
import lingogo.model.flashcard.Flashcard;
import lingogo.model.flashcard.LanguageType;
import lingogo.model.flashcard.Phrase;

/**
 * Imports the cards in the CSV file to the flashcard app.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String COMMAND_DESCRIPTION = "Imports flashcards from a CSV file into LingoGO!";

    public static final String[] COMMAND_PARAMETERS = new String[] {
        Parameter.CSV_FILE_NAME.withCondition("must exist in the data folder and have .csv extension.")
    };
    public static final String[] COMMAND_EXAMPLES = new String[] {
        COMMAND_WORD + " dictionary.csv"
    };

    public static final String MESSAGE_USAGE =
            getMessageUsage(COMMAND_WORD, COMMAND_DESCRIPTION, COMMAND_PARAMETERS, COMMAND_EXAMPLES);

    public static final String MESSAGE_SUCCESS = "LingoGO! has been updated with all the flashcards from %1$s.";

    public static final String MESSAGE_NO_CSV_ROW_ENTRIES_DETECTED = "There are no CSV row entries detected in %1$s!"
            + " Please recheck your CSV file!";

    public static final String MESSAGE_NOT_UPDATED =
            "LingoGO! already contains all the flashcards you are importing from %1$s.";

    public static final String IMPORT_IOEXCEPTION = "Could not load flashcards from %1$s into LingoGO!";

    private static final String[] csvHeaders = {"Language", "Foreign", "English"};
    private final String fileName;

    /**
     * @param fileName of the CSV file to be imported to the flashcard app
     */
    public ImportCommand(String fileName) {
        requireNonNull(fileName);

        this.fileName = fileName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.isSlideshowActive()) {
            throw new CommandException(Messages.MESSAGE_IN_SLIDESHOW_MODE);
        }

        String filepath = "data/" + fileName;

        File f = new File(filepath);
        if (!f.exists()) {
            throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, fileName));
        }

        // Get CSV file content
        List<String[]> content;
        try {
            content = importCsvFileContent(filepath, csvHeaders);
        } catch (CsvColumnHeaderException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_CSV_HEADERS, fileName));
        } catch (CsvValidationException | CsvNumColumnsException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_CSV_CONTENT, fileName));
        } catch (IOException ioe) {
            throw new CommandException(String.format(IMPORT_IOEXCEPTION, fileName));
        }

        // No CSV row entries detected
        if (content.size() == 0) {
            throw new CommandException(String.format(MESSAGE_NO_CSV_ROW_ENTRIES_DETECTED, fileName));
        }

        // Validate that all CSV row entries make up valid flashcards
        List<Flashcard> validatedFlashcards = validateCsvContentAreValidFlashcards(content);

        // Only when entire CSV file are valid flashcards do we add these entries into the actual flashcard list
        boolean isUpdated = false;
        for (Flashcard card : validatedFlashcards) {
            if (!model.hasFlashcard(card)) {
                isUpdated = true;
                model.addFlashcard(card);
            }
        }

        if (isUpdated) {
            return new CommandResult(String.format(MESSAGE_SUCCESS, fileName));
        }
        return new CommandResult(String.format(MESSAGE_NOT_UPDATED, fileName));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ImportCommand // instanceof handles nulls
                && fileName.equals(((ImportCommand) other).fileName)); // state check
    }

    private List<Flashcard> validateCsvContentAreValidFlashcards(List<String[]> content)
        throws CommandException {
        ArrayList<Flashcard> importedFlashcardList = new ArrayList<>();
        for (String[] line : content) {
            String languageType = line[0];
            String englishPhrase = line[2];
            String foreignPhrase = line[1];
            Flashcard card;

            try {
                card = new Flashcard(
                        new LanguageType(languageType), new Phrase(englishPhrase), new Phrase(foreignPhrase));
            } catch (IllegalArgumentException e) {
                throw new CommandException(String.format(MESSAGE_INVALID_CSV_CONTENT, fileName));
            }

            importedFlashcardList.add(card);
        }

        return importedFlashcardList;
    }
}
