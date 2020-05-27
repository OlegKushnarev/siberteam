package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

public class ClassNameChecker extends ArgChecker {
    public ClassNameChecker(String opt) {
        super(opt);
    }

    @Override
    protected void checkOptionValue(String className) throws InvalidInputArgException {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new InvalidInputArgException("Class " + className + " not found", e);
        }
    }
}