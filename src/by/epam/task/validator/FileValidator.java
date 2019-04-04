package by.epam.task.validator;

import java.io.File;

public class FileValidator {
    public boolean isValid(final File file) {
        return file.exists();
    }
}
