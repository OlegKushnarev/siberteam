package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class InputFilePathChecker extends ArgChecker {

    public InputFilePathChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws InvalidInputArgException {
        Path pathFile = Paths.get(optionValue);
        Predicate<Path> canReadFile = path -> Files.isRegularFile(path) && Files.isReadable(path);
        if (canReadFile.negate().test(pathFile)) {
            throw new InvalidInputArgException("The input file must be exist, must be readable, must be text");
        }
    }
}