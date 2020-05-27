package ru.siberteam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.arg.Args;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Args programArgs = new Args();
        if (!programArgs.parse(args)) {
            System.exit(0);
        }
        Sorters sorters = new Sorters(programArgs.getInputFile(), programArgs.getOutputFile(), programArgs.getSorters());
        if (!sorters.sortWords()) {
            LOG.error("Sort failed!");
        }
    }
}