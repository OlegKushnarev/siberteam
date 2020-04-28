package ru.siberteam;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.checker.ArgChecker;
import ru.siberteam.checker.InputFilePathChecker;
import ru.siberteam.checker.LimitChecker;
import ru.siberteam.checker.OutputFilePathChecker;
import ru.siberteam.statistic.TextStatistic;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "Input file name");
        options.addRequiredOption("o", "outputFile", true, "Output file name");
        options.addOption("l", "output limitation", true, "Print N first results");

        CommandLine cmd = null;
        int outputLimitation = 0;

        try {
            CommandLineParser parser = new DefaultParser();
            cmd = parser.parse(options, args);

            ArgChecker inputFilePathChecker = new InputFilePathChecker("i");
            inputFilePathChecker.check(cmd);

            ArgChecker outputFilePathChecker = new OutputFilePathChecker("o");
            outputFilePathChecker.check(cmd);

            if (cmd.hasOption("l")) {
                ArgChecker limitChecker = new LimitChecker("l");
                limitChecker.check(cmd);
                outputLimitation = Integer.parseInt(cmd.getOptionValue("l"));
            }
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("ru.siberteam.Main", options);
            System.exit(0);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid input args :", e);
            System.exit(0);
        }

        TextStatistic textStatistic = new TextStatistic();
        textStatistic.collectCharStatistic(cmd.getOptionValue("i"));
        textStatistic.setPercentFormat("##.#", '.');
        textStatistic.writeToFile(cmd.getOptionValue("o"), outputLimitation);
    }
}