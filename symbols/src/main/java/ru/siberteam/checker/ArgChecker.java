package ru.siberteam.checker;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.exception.InvalidInputArgException;

public abstract class ArgChecker {
    private static final Logger LOG = LogManager.getLogger(ArgChecker.class);

    private final String opt;

    public ArgChecker(String opt) {
        this.opt = opt;
    }

    protected abstract void checkOptionValue(String optionValue) throws InvalidInputArgException;

    public boolean check(CommandLine cmd) {
        boolean canCheck = true;
        try {
            this.checkOptionValue(cmd.getOptionValue(this.opt));
        } catch (InvalidInputArgException e) {
            LOG.error("Invalid input args:", e);
            canCheck = false;
        }
        return canCheck;
    }
}