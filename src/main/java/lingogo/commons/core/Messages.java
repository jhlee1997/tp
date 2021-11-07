package lingogo.commons.core;

import javafx.scene.control.Alert.AlertType;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command!";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX = "The flashcard index provided is invalid.";
    public static final String MESSAGE_FLASHCARDS_LISTED_OVERVIEW = "%1$d flashcards listed!";
    public static final String MESSAGE_INVALID_CSV_CONTENT = "The flashcards in %1$s are not in the correct format.";
    public static final String MESSAGE_INVALID_CSV_HEADERS = "The headers in %1$s are not in the correct format.";
    public static final String MESSAGE_FILE_NOT_FOUND = "%1$s cannot be found in the data folder.";
    public static final String MESSAGE_INVALID_CSV_FILE_NAME = "%1$s is not a valid CSV file name.";
    public static final String MESSAGE_IN_SLIDESHOW_MODE = "This command can only be run when slideshow mode is not"
            + " active!";
    public static final String MESSAGE_NOT_IN_SLIDESHOW_MODE = "This command can only be run when slideshow mode is"
            + " active!";
    public static final String MESSAGE_FLASHCARD_ALREADY_ANSWERED = "This flashcard has already been answered!";
    public static final String MESSAGE_INDEX_IS_NOT_NON_ZERO_UNSIGNED_INT = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INVALID_INDEX_RANGE = "The index range provided is invalid, exactly 2 indices "
            + "must be given where the second given index is larger or equal to the first index.";
    public static final String MESSAGE_INVALID_N = "Input n has to be an integer that is greater than 0 but less than "
            + "the total number of flashcards in the list.";

    public static class AlertMessage {

        public static final AlertMessage ALERT_WELCOME_TO_LINGOGO = new AlertMessage("Welcome to LingoGO!",
                "Welcome!", "It seems like you have no flashcards yet, let's get you started with a set of sample "
                + "flashcards!", AlertType.INFORMATION);

        public static final AlertMessage ALERT_INVALID_DATA_FILE = new AlertMessage("Invalid data file", "Warning!",
                "The data file is invalid. Will be starting with an empty flashcard list. Do exit "
                + "the application via the close button and fix the flashcardapp.json file in the data folder. "
                + "Bugs in data/flashcardapp.json include null values, missing commas or brackets, missing fields or "
                + "duplicate flashcards, among other issues. Any further commands will delete all data stored in "
                + "data/flashcardapp.json permanently.",
                AlertType.WARNING);

        public static final AlertMessage ALERT_PROBLEM_WHILE_READING_DATA_FILE = new AlertMessage("Problem while "
                + "reading data file", "Warning!", "There was a problem while reading from the file. Will be starting "
                + "with an empty flashcard list. Do exit the application via the close button before "
                + "restarting it again. Any further commands will delete all data stored in data/flashcardapp.json "
                + "permanently.", AlertType.WARNING);

        public static final AlertMessage ALERT_FLASHCARDAPP_JSON_IS_DIRECTORY = new AlertMessage("Invalid data file",
             "Warning!", "Please delete the directory at path: data/flashcardapp.json before restarting this "
            + "application. This path is reserved for storing LingoGO! data in a JSON file format. As such no further "
            + "commands will work until the directory named flashcardapp.json has been deleted, and the application "
            + "successfully restarted.", AlertType.WARNING);

        private String title;
        private String headerText;
        private String contentText;
        private AlertType alertType;

        private AlertMessage(String title, String headerText, String contentText, AlertType alertType) {
            this.title = title;
            this.headerText = headerText;
            this.contentText = contentText;
            this.alertType = alertType;
        }

        public String getTitle() {
            return title;
        }

        public String getHeaderText() {
            return headerText;
        }

        public String getContentText() {
            return contentText;
        }

        public AlertType getAlertType() {
            return alertType;
        }
    }




}

