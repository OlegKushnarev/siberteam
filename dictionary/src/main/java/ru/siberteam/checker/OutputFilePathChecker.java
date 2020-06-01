package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

public class OutputFilePathChecker extends ArgChecker {

    public OutputFilePathChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(List<String> optionValues) throws InvalidInputArgException {
        for (String optionValue : optionValues) {
            Path filePath = Paths.get(optionValue);
            if (Files.exists(filePath)) {
                Predicate<Path> canWriteFiles = path -> Files.isRegularFile(path) && Files.isWritable(path);
                if (canWriteFiles.negate().test(filePath)) {
                    throw new InvalidInputArgException("The output file must be writable, must be text");
                }
            } else {
                try {
                    Files.createFile(filePath);
                } catch (IOException e) {
                    throw new InvalidInputArgException("Cannot create the file " + optionValue, e);
                }
            }
        }
    }
}