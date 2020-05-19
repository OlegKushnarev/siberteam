package ru.siberteam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.arg.Args;
import ru.siberteam.sorter.Sorter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Args programArgs = new Args();
        if (!programArgs.parse(args)) {
            System.exit(0);
        }
        Sorter sorter = programArgs.getSorter();
        if (sorter == null) {
            System.exit(0);
        }
        try (Stream<String> stringStream = Files.lines(Paths.get(programArgs.getInputFile()))) {
            Stream<String> strStream = sorter.sortWords(stringStream);
            Files.write(Paths.get(programArgs.getOutputFile()), (Iterable<String>) strStream::iterator);
        } catch (IOException e) {
            LOG.error("Error working with file!", e);
        }
    }
}