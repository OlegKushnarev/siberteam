package ru.siberteam;

import ru.siberteam.arg.Args;
import ru.siberteam.statistic.TextStatistic;

public class Main {
    public static void main(String[] args) {
        Args programArgs = new Args();

        if (!programArgs.parser(args) || !programArgs.checkArgs()) {
            System.exit(0);
        }

        TextStatistic textStatistic = new TextStatistic();
        textStatistic.collectCharStatistic(programArgs.getInputFile(), programArgs.getSortMode());
        textStatistic.writeToFile(programArgs.getOutputFile(), programArgs.getOutputLimitation());
    }
}