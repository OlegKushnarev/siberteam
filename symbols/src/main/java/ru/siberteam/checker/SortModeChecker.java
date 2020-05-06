package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;
import ru.siberteam.sort.SortMode;

public class SortModeChecker extends ArgChecker {

    public SortModeChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws InvalidInputArgException {
        try {
            SortMode.valueOf(optionValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidInputArgException("The sort mode must be set with " + SortMode.possibleValues(), e);
        }
    }
}