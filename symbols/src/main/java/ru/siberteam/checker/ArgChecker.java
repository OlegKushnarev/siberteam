package ru.siberteam.checker;

import org.apache.commons.cli.CommandLine;

public abstract class ArgChecker {
    private String opt;

    public ArgChecker(String opt) {
        this.opt = opt;
    }

    public abstract void checkOptionValue(String optionValue) throws IllegalArgumentException;

    public void check(CommandLine cmd) throws IllegalArgumentException {
        if (cmd.hasOption(this.opt)) {
            this.checkOptionValue(cmd.getOptionValue(this.opt));
        }
    }
}