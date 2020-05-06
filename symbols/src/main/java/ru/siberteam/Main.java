package ru.siberteam;

import org.apache.commons.cli.Option;
import ru.siberteam.arg.Args;
import ru.siberteam.sort.SortMode;
import ru.siberteam.statistic.CharStatistic;
import ru.siberteam.statistic.TextStatistic;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Args programArgs = new Args();
        programArgs.addRequiredOption("i", "inputFile", true, "Input file name");
        programArgs.addRequiredOption("o", "outputFile", true, "Output file name");
        programArgs.addOption(new Option("l", "outputLimitation", true, "Print N first results"));
        programArgs.addOption(new Option("s", "sortMode", true, SortMode.description()));

        if (!programArgs.parser(args) || !programArgs.checkArgs()) {
            System.exit(0);
        }

        TextStatistic textStatistic = new TextStatistic();

        String value = programArgs.getValue("s");
        if (!value.isEmpty()) {
            Comparator<CharStatistic> sortMode = SortMode.valueOf(value).getSortFunction();
            textStatistic.setSortMode(sortMode);
        }

        textStatistic.collectCharStatistic(programArgs.getValue("i"));

        int outputLimitation = 0;
        value = programArgs.getValue("l");
        if (!value.isEmpty()) {
            outputLimitation = Integer.parseInt(value);
        }
        textStatistic.writeToFile(programArgs.getValue("o"), outputLimitation);
    }
}