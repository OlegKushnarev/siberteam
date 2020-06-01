package ru.siberteam.arg;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.checker.ArgChecker;
import ru.siberteam.checker.InputFilePathChecker;
import ru.siberteam.checker.OutputFilePathChecker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Args {
    private static final Logger LOG = LogManager.getLogger(Args.class);

    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        options.addOption(Option.builder("i")
                .desc("Input file names")
                .longOpt("inputFiles")
                .required()
                .hasArgs()
                .build());
        options.addRequiredOption("o", "outputFile", true, "Output file name");
    }

    private Map<String, ArgChecker> mapChecker() {
        Map<String, ArgChecker> checkerMap = new HashMap<>();
        checkerMap.put("i", new InputFilePathChecker("i"));
        checkerMap.put("o", new OutputFilePathChecker("o"));
        return checkerMap;
    }

    public boolean parse(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(100, this.getClass().getName(), " ", options,
                    System.lineSeparator() + e.getMessage(), true);
            return false;
        }
        Map<String, ArgChecker> checkerMap = mapChecker();
        return Arrays.stream(cmd.getOptions())
                .filter(option -> checkerMap.containsKey(option.getOpt()))
                .map(option -> checkerMap.get(option.getOpt()))
                .allMatch(checker -> checker.check(cmd));
    }

    public Set<String> getInputFiles() {
        return Arrays.stream(cmd.getOptionValues("i"))
                .collect(Collectors.toSet());
    }

    public String getOutputFile() {
        return cmd.getOptionValue("o");
    }
}
