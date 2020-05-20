package ru.siberteam.arg;

import org.apache.commons.cli.*;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.siberteam.checker.ArgChecker;
import ru.siberteam.checker.ClassNameChecker;
import ru.siberteam.checker.InputFilePathChecker;
import ru.siberteam.checker.OutputFilePathChecker;
import ru.siberteam.sorter.Sorter;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Args {
    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");
        options.addRequiredOption("c", "className", true, sortDescription());
    }

    private Sorter createSorter(Class<?> clazz) throws ReflectiveOperationException {
        Constructor<?> nativeConstructor = clazz.getDeclaredConstructor();
        return (Sorter) nativeConstructor.newInstance();
    }

    private Sorter sorter(Class<? extends Sorter> clazz) {
        try {
            return createSorter(clazz);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private String sortDescription() {
        return new Reflections("ru.siberteam.sorter", new SubTypesScanner(false))
                .getSubTypesOf(Sorter.class)
                .stream()
                .map(this::sorter)
                .filter(Objects::nonNull)
                .map(Sorter::sortDescription)
                .collect(Collectors.joining(" "));
    }

    private Map<String, ArgChecker> mapChecker() {
        Map<String, ArgChecker> checkerMap = new HashMap<>();
        checkerMap.put("i", new InputFilePathChecker("i"));
        checkerMap.put("o", new OutputFilePathChecker("o"));
        checkerMap.put("c", new ClassNameChecker("c"));
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

    public String getInputFile() {
        return cmd.getOptionValue("i");
    }

    public String getOutputFile() {
        return cmd.getOptionValue("o");
    }

    public Sorter getSorter() throws ReflectiveOperationException {
        Class<?> clazz = Class.forName(cmd.getOptionValue("c"));
        return createSorter(clazz);
    }
}
