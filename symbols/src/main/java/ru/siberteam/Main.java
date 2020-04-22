package ru.siberteam;

import org.apache.commons.cli.*;
import ru.siberteam.statistics.CharacterStatistics;

public class Main {
    public static void main(String[] args) {
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");

        String inputFileName = null;
        String outputFileName = null;
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("i")) {
                inputFileName = cmd.getOptionValue("i");
            }
            if (cmd.hasOption("o")) {
                outputFileName = cmd.getOptionValue("o");
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ru.siberteam.Main", options);
            System.exit(0);
        }

        CharacterStatistics characterStatistics = new CharacterStatistics(inputFileName);
        characterStatistics.writeToFile(outputFileName);
    }
}