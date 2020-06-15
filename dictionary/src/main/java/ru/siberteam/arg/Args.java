package ru.siberteam.arg;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.checker.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
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
        options.addOption("n", "numbersThreads", true, "Input number of threads.");
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
        checkerMap.put("n", new NumberChecker("n"));
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

    private Set<String> getUrlStrings() {
        if (cmd.hasOption("i")) {
            try (Stream<String> stringStream = Files.lines(Paths.get(cmd.getOptionValue("i")))) {
                return stringStream
                        .flatMap(str -> Arrays.stream(str.split(" ")))
                        .collect(Collectors.toSet());
            } catch (IOException e) {
                LOG.error("Unsuccessful attempt to read file {}", cmd.getOptionValue("i"), e);
                throw new IllegalStateException(e);
            }
        }
        return Arrays.stream(cmd.getOptionValues("u"))
                .collect(Collectors.toSet());
    }

    public Set<URL> getURLs() {
        return getUrlStrings().stream()
                .map(this::createURL)
                .collect(Collectors.toSet());
    }

    public String getOutputFile() {
        return cmd.getOptionValue("o");
    }

    public int getNumberThreads() {
        if (cmd.hasOption("n")) {
            return Integer.parseInt(cmd.getOptionValue("n"));
        }
        return -1;
    }

//    public ForkJoinPool getThreadPool() {
//        if (cmd.hasOption("n")) {
//            return new ForkJoinPool(Integer.parseInt(cmd.getOptionValue("n")));
//        }
//        return ForkJoinPool.commonPool();
//    }
}
