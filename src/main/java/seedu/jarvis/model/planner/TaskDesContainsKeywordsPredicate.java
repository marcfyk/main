package seedu.jarvis.model.planner;

import seedu.jarvis.commons.util.StringUtil;
import seedu.jarvis.model.planner.tasks.Task;

import java.util.List;
import java.util.function.Predicate;

/**
 * Tests that the task description of a {@code Task} matches any of
 * the keywords given.
 */
//TODO tests
public class TaskDesContainsKeywordsPredicate implements Predicate<Task> {
    private final List<String> keywords;

    public TaskDesContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Task task) {
        return keywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(task.getTaskDes(), keyword));
    }

    @Override
    public boolean equals(Object other) {
        return other == this //short circuit if same object
                || (other instanceof TaskDesContainsKeywordsPredicate //instanceof handles nulls
                && keywords.equals(((TaskDesContainsKeywordsPredicate) other).keywords)); //state check
    }
}
