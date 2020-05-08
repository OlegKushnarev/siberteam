package ru.siberteam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.arg.Args;
import ru.siberteam.statistic.TextStatistic;

import java.io.IOException;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Args programArgs = new Args();

        if (!programArgs.parse(args)) {
            System.exit(0);
        }

        TextStatistic textStatistic = new TextStatistic();

        try {
            textStatistic.collectCharStatistic(programArgs.getInputFile(), programArgs.getSortMode());
            textStatistic.writeToFile(programArgs.getOutputFile(), programArgs.getOutputLimitation());
        } catch (IOException e) {
            LOG.error("Error working with file!", e);
        }
    }
}