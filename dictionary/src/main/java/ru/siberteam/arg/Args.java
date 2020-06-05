package ru.siberteam.arg;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.checker.ArgChecker;
import ru.siberteam.checker.InputFilePathChecker;
import ru.siberteam.checker.OutputFilePathChecker;
import ru.siberteam.checker.UrlChecker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Args {
    private static final Logger LOG = LogManager.getLogger(Args.class);

    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.setRequired(true);
        optionGroup.addOption(Option.builder("u")
                .desc("Enter a list of URLs")
                .longOpt("URLs")
                .hasArgs()
                .build());
        optionGroup.addOption(new Option("i", "inputFile", true, "Input file name with URLs"));
        options.addOptionGroup(optionGroup);
        options.addRequiredOption("o", "outputFile", true, "Output file name");
    }

    private Map<String, ArgChecker> mapChecker() {
        Map<String, ArgChecker> checkerMap = new HashMap<>();
        if (cmd.hasOption("u")) {
            checkerMap.put("u", new UrlChecker("u"));
        }
        if (cmd.hasOption("i")) {
            checkerMap.put("i", new InputFilePathChecker("i"));
        }
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

    private URL createURL(String urlString) {
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            LOG.error("Error creating URL {}", urlString, e);
            throw new IllegalStateException(e);
        }
    }

    private Set<String> getSetUrlStrings() {
        if (cmd.hasOption("u")) {
            return Arrays.stream(cmd.getOptionValues("u"))
                    .collect(Collectors.toSet());
        } else if (cmd.hasOption("i")) {
            try (Stream<String> stringStream = Files.lines(Paths.get(cmd.getOptionValue("i")))) {
                return stringStream
                        .flatMap(str -> Arrays.stream(str.split(" ")))
                        .collect(Collectors.toSet());
            } catch (IOException e) {
                LOG.error("Unsuccessful attempt to read file {}", cmd.getOptionValue("i"), e);
                throw new IllegalStateException(e);
            }
        }
        return new HashSet<>();
    }

    public Set<URL> getURLs() {
        return getSetUrlStrings().stream()
                .map(this::createURL)
                .collect(Collectors.toSet());
    }

    public String getOutputFile() {
        return cmd.getOptionValue("o");
    }
}
