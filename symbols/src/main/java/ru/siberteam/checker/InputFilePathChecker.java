package ru.siberteam.checker;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputFilePathChecker extends ArgChecker {
    private String opt;

    public InputFilePathChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws IllegalArgumentException {
        Path pathFile = Paths.get(optionValue);
        if (!(Files.isRegularFile(pathFile) &&
                Files.isReadable(pathFile))) {
            throw new IllegalArgumentException("The input file must be exist, must be readable, must be text");
        }
    }
}