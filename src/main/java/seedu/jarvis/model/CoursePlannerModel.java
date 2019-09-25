package seedu.jarvis.model;

import seedu.jarvis.model.course.Course;

public interface CoursePlannerModel {
    /**
     * Adds a module.
     */
    void addModule(Course course);

    /**
     * Deletes the given course.
     * The course must exist within the user's list.
     */
    void deleteModule(Course course);

    /**
     * Checks if the user can take this course.
     */
    boolean canTakeModule(Course course);
}
