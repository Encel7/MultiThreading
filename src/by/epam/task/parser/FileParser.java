package by.epam.task.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FileParser {
    public List<List<String>> parseFile(final List<String> fileList) {
        return fileList.stream().map(str -> Arrays.asList(
                str.split(" "))).collect(Collectors.toList());
    }
}
