package ru.siberteam.checker;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.exception.InvalidInputArgException;

import java.util.Arrays;
import java.util.List;

public abstract class ArgChecker {
    protected static final Logger LOG = LogManager.getLogger(OutputFilePathChecker.class);

    private final String opt;

    public ArgChecker(String opt) {
        this.opt = opt;
    }

    protected abstract void checkOptionValue(List<String> optionValues) throws InvalidInputArgException;

    public boolean check(CommandLine cmd) {
        if (!cmd.hasOption(opt)) {
            return false;
        }
        try {
            checkOptionValue(Arrays.asList(cmd.getOptionValues(opt)));
        } catch (InvalidInputArgException e) {
            LOG.error("Invalid input args:", e);
            return false;
        }
        return true;
    }
}
