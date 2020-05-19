package ru.siberteam.arg;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.checker.ArgChecker;
import ru.siberteam.checker.ClassNameChecker;
import ru.siberteam.checker.InputFilePathChecker;
import ru.siberteam.checker.OutputFilePathChecker;
import ru.siberteam.sorter.Sorter;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Args {
    private static final Logger LOG = LogManager.getLogger(Args.class);
    private final Options options = new Options();
    private CommandLine cmd;

    public Args() {
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");
        options.addRequiredOption("c", "className", true, sortDescription());
    }

    private String sortDescription() {
        return "Use \"ru.siberteam.sorter.AlphabeticalSorter\" to sort alphabetically." + System.lineSeparator() +
                "Use \"ru.siberteam.sorter.AlphabeticalInvertedWordSorter\" to sort alphabetically inverted words." + System.lineSeparator() +
                "Use \"ru.siberteam.sorter.FirstLetterSorter\" to sort words by first letter (vowel or consonant)." + System.lineSeparator() +
                "Use \"ru.siberteam.sorter.VowelNumberSorter\" to sort  words by the number of vowels in a word." + System.lineSeparator() +
                "Use \"ru.siberteam.sorter.WordLengthSorter\" to sort by word length.";
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
            formatter.printHelp(this.getClass().getName(), " ", options,
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

    public Sorter getSorter() {
        try {
            Class<?> clazz = Class.forName(cmd.getOptionValue("c"));
            Constructor<?> nativeConstructor = clazz.getDeclaredConstructor();
            sortDescription();
            return (Sorter) nativeConstructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error("The specified class {} object cannot be instantiated.", cmd.getOptionValue("c"), e);
            return null;
        }
    }
}
