package by.epam.task.reader;

import by.epam.task.exception.FileException;
import by.epam.task.validator.FileValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader {
    private static final Logger LOG = LogManager.getLogger("name");

    public List<String> read(final File inputFile) throws FileException {
        List<String> mList = new ArrayList<>();
        FileValidator fileValidator = new FileValidator();
        if (fileValidator.isValid(inputFile)) {
            try (Stream<String> linesText = Files.lines((Paths.
                    get(inputFile.getPath()))).skip(0)) {
                linesText.forEach(mList::add);
            } catch (IOException e) {
                LOG.error("File ".concat(inputFile.getName()).
                        concat(" has problems with closing"));
                LOG.error(e.getStackTrace());
            }
            return mList;
        } else {
            throw new FileException("File was incorrect");
        }

    }
}
