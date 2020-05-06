package ru.siberteam.arg;

import org.apache.commons.cli.*;
import ru.siberteam.checker.Checkers;

import java.util.Arrays;

public class Args {
    private final Options options = new Options();
    private CommandLine cmd;

    public void addOption(Option option) {
        options.addOption(option);
    }

    public void addRequiredOption(String opt, String longOpt, boolean hasArg, String description) {
        this.options.addRequiredOption(opt, longOpt, hasArg, description);
    }

    public boolean parser(String[] args) {
        boolean canParse = true;
        try {
            CommandLineParser parser = new DefaultParser();
            this.cmd = parser.parse(this.options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.getClass().getName(), " ", this.options,
                    System.lineSeparator() + e.getMessage(), true);
            canParse = false;
        }
        return canParse;
    }

    public boolean checkArgs() {
        return Arrays.stream(this.cmd.getOptions())
                .map(Option::getOpt)
                .map(opt -> Checkers.valueOf(opt.toUpperCase()).getChecker())
                .allMatch(checker -> checker.check(this.cmd));
    }

    public String getValue(String opt) {
        String value = this.cmd.getOptionValue(opt);
        return value == null ? "" : value;
    }
}