package ru.siberteam.arg;

import org.apache.commons.cli.*;
import ru.siberteam.checker.Checkers;
import ru.siberteam.sort.SortMode;
import ru.siberteam.statistic.CharStatistic;

import java.util.Arrays;
import java.util.Comparator;

public class Args {
    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");
        options.addOption(new Option("l", true, "Print N first results"));
        options.addOption(new Option("s", true, SortMode.description()));
    }

    public boolean parser(String[] args) {
        boolean canParse = true;
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.getClass().getName(), " ", options,
                    System.lineSeparator() + e.getMessage(), true);
            canParse = false;
        }
        return canParse;
    }

    public boolean checkArgs() {
        return Arrays.stream(cmd.getOptions())
                .map(Option::getOpt)
                .map(opt -> Checkers.valueOf(opt.toUpperCase()).getChecker())
                .allMatch(checker -> checker.check(cmd));
    }

    public String getInputFile() {
        return cmd.getOptionValue("i");
    }

    public String getOutputFile() {
        return cmd.getOptionValue("o");
    }

    public int getOutputLimitation() {
        String value = cmd.getOptionValue("l", "0");
        return Integer.parseInt(value);
    }

    public Comparator<CharStatistic> getSortMode() {
        String value = cmd.getOptionValue("s", "desc");
        return SortMode.valueOf(value.toUpperCase()).getSortOrder();
    }
}