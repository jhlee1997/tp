package lingogo.logic.commands;

import static java.util.Objects.requireNonNull;
import static lingogo.logic.parser.CliSyntax.PREFIX_INDEX_LIST;
import static lingogo.logic.parser.CliSyntax.PREFIX_INDEX_RANGE;
import static lingogo.logic.parser.CliSyntax.PREFIX_LANGUAGE_TYPE;
import static lingogo.model.Model.PREDICATE_SHOW_ALL_FLASHCARDS;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.util.Pair;
import lingogo.commons.core.Messages;
import lingogo.commons.core.index.Index;
import lingogo.commons.util.CollectionUtil;
import lingogo.logic.commands.exceptions.CommandException;
import lingogo.model.Model;
import lingogo.model.flashcard.Flashcard;
import lingogo.model.flashcard.FlashcardInGivenFlashcardListPredicate;
import lingogo.model.flashcard.LanguageTypeMatchesGivenPhrasePredicate;
import lingogo.model.flashcard.Phrase;

/**
 * Filters all flashcards in the flashcard app by its Language type.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";
    public static final String COMMAND_DESCRIPTION = "Filters all flashcards by a given filter";
    public static final String COMMAND_USAGE = "filter [l/LANGUAGE_TYPE] [i/INDEX_LIST] [r/INDEX_RANGE]";
    public static final String COMMAND_EXAMPLES = "filter l/Chinese\nfilter i/1 2 3\nfilter r/1 4\n"
        + "filter l/Chinese i/1 2 3\n"
        + "filter l/Chinese r/1 4";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Filters all flashcards by a given filter.\n"
            + "Parameters: "
            + "[" + PREFIX_LANGUAGE_TYPE + "LANGUAGE_TYPE] "
            + "[" + PREFIX_INDEX_LIST + "INDEX_LIST] "
            + "[" + PREFIX_INDEX_RANGE + "INDEX_RANGE] "
            + "Example: " + COMMAND_EXAMPLES;

    private final FilterBuilder filterBuilder;

    public FilterCommand(FilterBuilder filterBuilder) {
        this.filterBuilder = filterBuilder;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.isSlideshowActive()) {
            throw new CommandException(Messages.MESSAGE_IN_SLIDESHOW_MODE);
        }

        model.updateFilteredFlashcardList(filterBuilder.buildFilter(model));
        return new CommandResult(
                String.format(Messages.MESSAGE_FLASHCARDS_LISTED_OVERVIEW, model.getFilteredFlashcardList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FilterCommand // instanceof handles nulls
                && filterBuilder.equals(((FilterCommand) other).filterBuilder)); // state check
    }

    /**
     * Builds a filter predicate from the given inputs
     */
    public static class FilterBuilder {

        private Phrase languageType;
        private List<Index> indexList;
        private Pair<Index, Index> indexRangePair;


        public FilterBuilder() {

        }

        /**
         * Returns true if at least one field has been filtered.
         */
        public boolean isAnyFieldFiltered() {
            return CollectionUtil.isAnyNonNull(languageType, indexList, indexRangePair);
        }

        public void setLanguageType(Phrase languageType) {
            this.languageType = languageType;
        }

        public void setIndexList(List<Index> indexList) {
            this.indexList = indexList;
        }

        public void setIndexRangePair(Pair<Index, Index> indexRangePair) {
            this.indexRangePair = indexRangePair;
        }

        private Predicate<Flashcard> buildIndexFilter(Model model) throws CommandException {

            if (this.indexList == null) {
                return PREDICATE_SHOW_ALL_FLASHCARDS;
            }

            List<Flashcard> lastShownList = model.getFilteredFlashcardList();

            if (indexList.stream().anyMatch(index -> index.getZeroBased() >= lastShownList.size())) {
                throw new CommandException(Messages.MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX);
            }

            List<Flashcard> filteredList =
                    indexList.stream().map(index -> lastShownList.get(index.getZeroBased())).collect(
                    Collectors.toList());

            return new FlashcardInGivenFlashcardListPredicate(filteredList);
        }

        private Predicate<Flashcard> buildLanguageTypeFilter() {
            if (this.languageType == null) {
                return PREDICATE_SHOW_ALL_FLASHCARDS;
            }

            return new LanguageTypeMatchesGivenPhrasePredicate(languageType);
        }

        private Predicate<Flashcard> buildRangeFilter(Model model) throws CommandException {
            if (this.indexRangePair == null) {
                return PREDICATE_SHOW_ALL_FLASHCARDS;
            }

            List<Flashcard> lastShownList = model.getFilteredFlashcardList();

            if (indexRangePair.getKey().getZeroBased() >= lastShownList.size()
                    || indexRangePair.getValue().getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_FLASHCARD_DISPLAYED_INDEX);
            }

            List<Flashcard> filteredList = IntStream.range(indexRangePair.getKey().getZeroBased(),
                    indexRangePair.getValue().getOneBased()).boxed().map(integer -> lastShownList.get(integer))
                    .collect(Collectors.toList());

            return new FlashcardInGivenFlashcardListPredicate(filteredList);
        }


        /**
         * Builds the filter predicate from the given model and filter fields.
         *
         * @param model
         * @return filter predicate
         * @throws CommandException
         */
        public Predicate<Flashcard> buildFilter(Model model) throws CommandException {

            Predicate<Flashcard> languageTypeFilter = buildLanguageTypeFilter();
            Predicate<Flashcard> indexFilter = buildIndexFilter(model);
            Predicate<Flashcard> rangeFilter = buildRangeFilter(model);
            // filters out flashcards that are not in the current displayed flashcards list
            Predicate<Flashcard> currentDisplayedListFilter =
                    new FlashcardInGivenFlashcardListPredicate(model.getFilteredFlashcardList());


            return flashcard -> languageTypeFilter.test(flashcard) && indexFilter.test(flashcard)
                    && rangeFilter.test(flashcard) && currentDisplayedListFilter.test(flashcard);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof FilterBuilder)) {
                return false;
            }
            FilterBuilder that = (FilterBuilder) o;
            return Objects.equals(languageType, that.languageType) && Objects.equals(indexList,
                    that.indexList) && Objects.equals(indexRangePair, that.indexRangePair);
        }

        @Override
        public int hashCode() {
            return Objects.hash(languageType, indexList, indexRangePair);
        }
    }
}
