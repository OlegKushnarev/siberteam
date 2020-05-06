package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;
import ru.siberteam.sort.SortMode;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SortModeChecker extends ArgChecker {

    public SortModeChecker(String opt) {
        super(opt);
    }

    private String getPossibleValues() {
        return Arrays.stream(SortMode.values())
                .map(Enum::name)
                .collect(Collectors.joining(" "));
    }

    @Override
    public void checkOptionValue(String optionValue) throws InvalidInputArgException {
        try {
            SortMode.valueOf(optionValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidInputArgException("The sort mode must be set with " + this.getPossibleValues(), e);
        }
    }
}