package lingogo.model.flashcard;

import static java.util.Objects.requireNonNull;
import static lingogo.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

/**
 * Represents a Flashcard in LingoGO!.
 * Guarantees: details are present and not null; field values are validated; immutable.
 */
public class Flashcard {
    // Empty flashcard
    public static final Flashcard EMPTY_FLASHCARD = new Flashcard(new LanguageType("empty"),
            new Phrase("empty english phrase"), new Phrase("empty foreign phrase"));

    // Data fields
    private final LanguageType languageType; // to update UML diagram multiplicity if languageType is no longer Phrase
    private final Phrase englishPhrase;
    private final Phrase foreignPhrase;
    private final Boolean isFlipped;

    /**
     * Every field must be present and not null. {@code isFlipped} set to false by default.
     */
    public Flashcard(LanguageType languageType, Phrase englishPhrase, Phrase foreignPhrase) {
        requireAllNonNull(englishPhrase, foreignPhrase);
        this.languageType = languageType;
        this.englishPhrase = englishPhrase;
        this.foreignPhrase = foreignPhrase;
        this.isFlipped = false;
    }

    /**
     * Every field must be present and not null.
     */
    public Flashcard(LanguageType languageType, Phrase englishPhrase, Phrase foreignPhrase, Boolean isFlipped) {
        requireAllNonNull(englishPhrase, foreignPhrase, isFlipped);
        this.languageType = languageType;
        this.englishPhrase = englishPhrase;
        this.foreignPhrase = foreignPhrase;
        this.isFlipped = isFlipped;
    }

    /**
     * Copy constructor.
     */
    public Flashcard(Flashcard toCopy) {
        requireNonNull(toCopy);
        this.languageType = toCopy.languageType;
        this.englishPhrase = toCopy.englishPhrase;
        this.foreignPhrase = toCopy.foreignPhrase;
        this.isFlipped = toCopy.isFlipped;
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public Phrase getEnglishPhrase() {
        return englishPhrase;
    }

    public Phrase getForeignPhrase() {
        return foreignPhrase;
    }

    public Boolean getFlipStatus() {
        return isFlipped;
    }

    /**
     * Returns a flipped version of this flashcard.
     *
     * @return Flipped flashcard
     */
    public Flashcard getFlippedFlashcard() {
        return new Flashcard(languageType, englishPhrase, foreignPhrase, !isFlipped);
    }

    /**
     * Returns true if both flashcards have the matching English phrase, foreign phrase, foreign language and
     * language type. English phrase and language type matches are case-insensitive and disregard trailing spaces.
     * This defines a weaker notion of equality between two flashcards.
     */
    public boolean isSameFlashcard(Flashcard otherFlashcard) {
        if (otherFlashcard == this) {
            return true;
        }
        return otherFlashcard != null
            && new LanguageTypeMatchesGivenLanguageTypePredicate(this.languageType).test(otherFlashcard)
            && new EnglishPhraseMatchesGivenPhrasePredicate(this.englishPhrase).test(otherFlashcard)
            && otherFlashcard.getForeignPhrase().equals(getForeignPhrase());
    }

    /**
     * Returns true if both flashcards have the same foreign language, English phrase,
     * foreign phrase and flipStatus.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Flashcard)) {
            return false;
        }

        Flashcard otherFlashcard = (Flashcard) other;
        return otherFlashcard.getLanguageType().equals(getLanguageType())
            && otherFlashcard.getEnglishPhrase().equals(getEnglishPhrase())
            && otherFlashcard.getForeignPhrase().equals(getForeignPhrase())
            && otherFlashcard.getFlipStatus().equals(getFlipStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(languageType, englishPhrase, foreignPhrase);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n")
                .append("Language: ")
                .append(getLanguageType())
                .append("\n")
                .append("Foreign phrase: ")
                .append(getForeignPhrase())
                .append("\n")
                .append("English phrase: ")
                .append(getEnglishPhrase());

        return builder.toString();
    }
}
