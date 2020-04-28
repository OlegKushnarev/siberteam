package ru.siberteam.checker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OutputFilePathChecker extends ArgChecker {

    public OutputFilePathChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws IllegalArgumentException {
        Path pathFile = Paths.get(optionValue);
        if (!(Files.isRegularFile(pathFile) &&
                Files.isWritable(pathFile))) {
            throw new IllegalArgumentException("The output file must be exist, must be writable, must be text");
        }
    }
}