package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

public class ClassNameChecker extends ArgChecker {
    public ClassNameChecker(String opt) {
        super(opt);
    }

    @Override
    protected void checkOptionValue(String optionValue) throws InvalidInputArgException {
        try {
            Class.forName(optionValue);
        } catch (ClassNotFoundException e) {
            throw new InvalidInputArgException("Class " + optionValue + " not found", e);
        }
    }
}