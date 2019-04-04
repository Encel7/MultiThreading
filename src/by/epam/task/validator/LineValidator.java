package by.epam.task.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LineValidator {
    private static final Logger LOG = LogManager.getLogger("name");

    public boolean validLine(final List<String> parsedString) {
        Pattern correctNumberRegEx = Pattern.compile("[0-9]+[\\.]?[0-9]+");
        if (parsedString.size() == 8) {
            for (String element : parsedString) {
                if (parsedString.indexOf(element) != 8) {
                    Matcher matcher = correctNumberRegEx.matcher(element);
                    if (!matcher.matches()) {
                        LOG.error("Invalid data value: " + element);
                        return false;
                    }
                }
            }
        } else {
            LOG.error("Invalid number of parameters "
                    +
                    parsedString.size());
            return false;
        }
        return true;
    }
}
