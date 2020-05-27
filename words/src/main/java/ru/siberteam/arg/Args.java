package ru.siberteam.arg;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import ru.siberteam.checker.ArgChecker;
import ru.siberteam.checker.ClassNameChecker;
import ru.siberteam.checker.InputFilePathChecker;
import ru.siberteam.checker.OutputFilePathChecker;
import ru.siberteam.description.Description;
import ru.siberteam.sorter.Sorter;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Args {
    private static final Logger LOG = LogManager.getLogger(Args.class);

    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");
        OptionGroup optionGroup = new OptionGroup();
        optionGroup.addOption(new Option("c", "className", true, sortDescription()));
        optionGroup.addOption(new Option("a", true, "Use any value for all possible sorters"));
        options.addOptionGroup(optionGroup);
    }

    private Stream<Class<? extends Sorter>> sorterClassesStream() {
        return new Reflections("ru.siberteam.sorter", new SubTypesScanner(false))
                .getSubTypesOf(Sorter.class)
                .stream()
                .filter(clazz -> clazz.isAnnotationPresent(Description.class));
    }

    private String sortDescription() {
        return sorterClassesStream()
                .map(clazz -> clazz.getAnnotation(Description.class).value())
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private Map<String, ArgChecker> mapChecker() {
        Map<String, ArgChecker> checkerMap = new HashMap<>();
        checkerMap.put("i", new InputFilePathChecker("i"));
        checkerMap.put("o", new OutputFilePathChecker("o"));
        if (cmd.hasOption("c")) {
            checkerMap.put("c", new ClassNameChecker("c"));
        }
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

    private Sorter createSorter(Class<?> clazz) {
        try {
            Constructor<?> nativeConstructor = clazz.getDeclaredConstructor();
            return (Sorter) nativeConstructor.newInstance();
        } catch (ReflectiveOperationException e) {
            LOG.error("The specified class {} object cannot be instantiated.", clazz.getName(), e);
            throw new IllegalStateException(e);
        }
    }

    private Sorter getSorter() {
        try {
            Class<?> clazz = Class.forName(cmd.getOptionValue("c"));
            return createSorter(clazz);
        } catch (ClassNotFoundException e) {
            LOG.error("The class {} not found.", cmd.getOptionValue("c"), e);
            throw new IllegalStateException(e);
        }
    }

    public Set<Sorter> getSorters() {
        if (cmd.hasOption("c")) {
            return new LinkedHashSet(Collections.singletonList(getSorter()));
        }
        return sorterClassesStream()
                .map(this::createSorter)
                .collect(Collectors.toSet());
    }
}