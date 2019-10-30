package seedu.jarvis.logic.commands.address;

import static java.util.Objects.requireNonNull;
import static seedu.jarvis.model.address.AddressModel.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.jarvis.commons.core.Messages;
import seedu.jarvis.commons.core.index.Index;
import seedu.jarvis.logic.commands.Command;
import seedu.jarvis.logic.commands.CommandResult;
import seedu.jarvis.logic.commands.exceptions.CommandException;
import seedu.jarvis.model.Model;
import seedu.jarvis.model.address.person.Person;
import seedu.jarvis.storage.history.commands.JsonAdaptedCommand;
import seedu.jarvis.storage.history.commands.address.JsonAdaptedDeleteAddressCommand;
import seedu.jarvis.storage.history.commands.exceptions.InvalidCommandToJsonException;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteAddressCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public static final String MESSAGE_INVERSE_SUCCESS_ADD = "New person added: %1$s";
    public static final String MESSAGE_INVERSE_PERSON_TO_ADD_ALREADY_EXIST = "Person already added: %1$s";

    public static final boolean HAS_INVERSE = true;

    private final Index targetIndex;

    private Person deletedPerson;

    /**
     * Creates a {@code DeleteAddressCommand} and sets targetIndex to the {@code Index} and {@code Person} that was
     * deleted, which is null if person has not been deleted.
     *
     * @param targetIndex {@code Index} of the {@code Person} to be deleted.
     * @param deletedPerson {@code Person} that was deleted, which is null if person has not been deleted.
     */
    public DeleteAddressCommand(Index targetIndex, Person deletedPerson) {
        this.targetIndex = targetIndex;
        this.deletedPerson = deletedPerson;
    }

    /**
     * Creates a {@code DeleteAddressCommand} and sets targetIndex to the {@code Index}
     * of the {@code Person} to be deleted.
     *
     * @param targetIndex of the {@code Person} to be deleted.
     */
    public DeleteAddressCommand(Index targetIndex) {
        this(targetIndex, null);
    }


    /**
     * Gets the command word of the command.
     *
     * @return {@code String} representation of the command word.
     */
    @Override
    public String getCommandWord() {
        return COMMAND_WORD;
    }

    /**
     * Gets the {@code Index} of the {@code Person} to be deleted.
     *
     * @return {@code Index} of the {@code Person} to be deleted.
     */
    public Index getTargetIndex() {
        return targetIndex;
    }

    /**
     * Gets the {@code Person} that was deleted, which is null if the person has not been deleted yet.
     *
     * @return {@code Optional} of {@code Person} that was deleted, empty if person has not been deleted.
     */
    public Optional<Person> getDeletedPerson() {
        return Optional.ofNullable(deletedPerson);
    }

    /**
     * Returns whether the command has an inverse execution.
     * If the command has no inverse execution, then calling {@code executeInverse}
     * will be guaranteed to always throw a {@code CommandException}.
     *
     * @return Whether the command has an inverse execution.
     */
    @Override
    public boolean hasInverseExecution() {
        return HAS_INVERSE;
    }

    /**
     * Deletes {@code Person} from address book.
     *
     * @param model {@code Model} which the command should operate on.
     * @return {@code CommandResult} of a successful delete.
     * @throws CommandException If targetIndex is >= the number of persons in address book.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        deletedPerson = lastShownList.get(targetIndex.getZeroBased());
        model.deletePerson(deletedPerson);

        return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, deletedPerson));
    }

    /**
     * Adds back the {@code Person} that was deleted.
     *
     * @param model {@code Model} which the command should inversely operate on.
     * @return {@code CommandResult} of a successful restore of the deleted {@code Person} if the {@code Person} is
     * not already inside the address book.
     * @throws CommandException If the person to be added will be in conflict with an existing person in the
     * address book.
     */
    @Override
    public CommandResult executeInverse(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasPerson(deletedPerson)) {
            throw new CommandException(String.format(MESSAGE_INVERSE_PERSON_TO_ADD_ALREADY_EXIST, deletedPerson));
        }

        model.addPerson(targetIndex.getZeroBased(), deletedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(String.format(MESSAGE_INVERSE_SUCCESS_ADD, deletedPerson));
    }

    /**
     * Gets a {@code JsonAdaptedCommand} from a {@code Command} for local storage purposes.
     *
     * @return {@code JsonAdaptedCommand}.
     * @throws InvalidCommandToJsonException If command should not be adapted to JSON format.
     */
    @Override
    public JsonAdaptedCommand adaptToJsonAdaptedCommand() throws InvalidCommandToJsonException {
        return new JsonAdaptedDeleteAddressCommand(this);
    }

    @Override
    public boolean equals(Object obj) {
        // Short circuit if it is the same object.
        if (obj == this) {
            return true;
        }
        // instanceof handles nulls.
        if (!(obj instanceof DeleteAddressCommand)) {
            return false;
        }
        DeleteAddressCommand other = (DeleteAddressCommand) obj;
        return targetIndex.equals(other.targetIndex) && Objects.equals(deletedPerson, other.deletedPerson);
    }
}
