package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;

public class OutputFilePathChecker extends ArgChecker {

    public OutputFilePathChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws InvalidInputArgException {
        Path pathFile = Paths.get(optionValue);
        Predicate<Path> canWriteFile = path -> Files.isRegularFile(path) && Files.isWritable(path);
        if (canWriteFile.negate().test(pathFile)) {
            throw new InvalidInputArgException("The output file must be exist, must be writable, must be text");
        }
    }
}