package ru.siberteam.checker;

public enum Checkers {
    I(new InputFilePathChecker("i")),
    O(new OutputFilePathChecker("o")),
    L(new LimitChecker("l")),
    S(new SortModeChecker("s"));

    private final ArgChecker checker;

    public ArgChecker getChecker() {
        return this.checker;
    }

    Checkers(ArgChecker checker) {
        this.checker = checker;
    }
}