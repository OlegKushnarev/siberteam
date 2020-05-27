package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

import java.io.IOException;
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
        Path filePath = Paths.get(optionValue);
        if (Files.exists(filePath)) {
            Predicate<Path> canWriteFile = path -> Files.isRegularFile(path) && Files.isWritable(path);
            if (canWriteFile.negate().test(filePath)) {
                throw new InvalidInputArgException("The output file must be writable, must be text");
            }
        } else {
            try {
                Files.createFile(filePath);
                Files.delete(filePath);
            } catch (IOException e) {
                throw new InvalidInputArgException("Cannot create the file " + optionValue, e);
            }
        }

    }
}