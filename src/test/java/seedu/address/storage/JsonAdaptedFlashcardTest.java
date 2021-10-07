package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedFlashcard.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalFlashcards.GOOD_NIGHT_CHINESE_FLASHCARD;

import org.junit.jupiter.api.Test;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.flashcard.Phrase;

public class JsonAdaptedFlashcardTest {
    private static final String INVALID_ENGLISH_PHRASE = "";
    private static final String INVALID_FOREIGN_PHRASE = "";

    private static final String VALID_ENGLISH_PHRASE = GOOD_NIGHT_CHINESE_FLASHCARD.getEnglishPhrase().toString();
    private static final String VALID_FOREIGN_PHRASE = GOOD_NIGHT_CHINESE_FLASHCARD.getForeignPhrase().toString();

    @Test
    public void toModelType_validFlashcardDetails_returnsFlashcard() throws Exception {
        JsonAdaptedFlashcard flashcard = new JsonAdaptedFlashcard(GOOD_NIGHT_CHINESE_FLASHCARD);
        assertEquals(GOOD_NIGHT_CHINESE_FLASHCARD, flashcard.toModelType());
    }

    @Test
    public void toModelType_invalidEnglishPhrase_throwsIllegalValueException() {
        JsonAdaptedFlashcard flashcard =
            new JsonAdaptedFlashcard(INVALID_ENGLISH_PHRASE, VALID_FOREIGN_PHRASE);
        String expectedMessage = Phrase.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, flashcard::toModelType);
    }

    @Test
    public void toModelType_nullEnglishPhrase_throwsIllegalValueException() {
        JsonAdaptedFlashcard flashcard = new JsonAdaptedFlashcard(null, VALID_FOREIGN_PHRASE);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "English " + Phrase.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, flashcard::toModelType);
    }

    @Test
    public void toModelType_invalidForeignPhrase_throwsIllegalValueException() {
        JsonAdaptedFlashcard flashcard =
            new JsonAdaptedFlashcard(VALID_ENGLISH_PHRASE, INVALID_FOREIGN_PHRASE);
        String expectedMessage = Phrase.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, flashcard::toModelType);
    }

    @Test
    public void toModelType_nullForeignPhrase_throwsIllegalValueException() {
        JsonAdaptedFlashcard flashcard = new JsonAdaptedFlashcard(VALID_ENGLISH_PHRASE, null);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "Foreign " + Phrase.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, flashcard::toModelType);
    }

}
