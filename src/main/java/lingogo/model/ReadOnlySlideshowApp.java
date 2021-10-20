package lingogo.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import lingogo.model.flashcard.Flashcard;

/**
 * Unmodifiable view of a slideshow
 */
public interface ReadOnlySlideshowApp {
    /**
     * Returns the current flashcard to be displayed in the slideshow app.
     */
    Flashcard getCurrentSlide();

    /**
     * Returns the currentSlide property of the slideshow app.
     */
    ObjectProperty<Flashcard> getCurrentSlideProperty();

    /**
     * Returns the isActive boolean property of the slideshow app.
     */
    BooleanProperty isActiveProperty();

    /**
     * Returns the isAnswerDisplayedProperty of the slideshow app.
     */
    BooleanProperty isAnswerDisplayedProperty();
}
