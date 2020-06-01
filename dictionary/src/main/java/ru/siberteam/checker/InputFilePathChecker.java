package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class InputFilePathChecker extends ArgChecker {

    public InputFilePathChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(List<String> optionValues) throws InvalidInputArgException {
        Predicate<List<String>> canReadFiles = values -> values.stream()
                .map(Paths::get)
                .allMatch(path -> Files.isRegularFile(path) && Files.isReadable(path));
        if (canReadFiles.negate().test(optionValues)) {
            throw new InvalidInputArgException("The input file must be exist, must be readable, must be text");
        }
    }
}
