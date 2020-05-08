package ru.siberteam.arg;

import org.apache.commons.cli.*;
import ru.siberteam.checker.*;
import ru.siberteam.sort.SortMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Args {
    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");
        options.addOption(new Option("l", true, "Print N first results"));
        options.addOption(new Option("s", true, SortMode.description()));
    }

    private Map<String, ArgChecker> mapChecker() {
        Map<String, ArgChecker> checkerMap = new HashMap<>();
        checkerMap.put("i", new InputFilePathChecker("i"));
        checkerMap.put("o", new OutputFilePathChecker("o"));
        checkerMap.put("l", new LimitChecker("l"));
        checkerMap.put("s", new SortModeChecker("s"));
        return checkerMap;
    }

    public boolean parse(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(this.getClass().getName(), " ", options,
                    System.lineSeparator() + e.getMessage(), true);
            return false;
        }
        Map<String, ArgChecker> checkerMap = mapChecker();
        return Arrays.stream(cmd.getOptions())
                .map(option -> checkerMap.get(option.getOpt()))
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

    public SortMode getSortMode() {
        String value = cmd.getOptionValue("s", "desc");
        return SortMode.valueOf(value.toUpperCase());
    }
}