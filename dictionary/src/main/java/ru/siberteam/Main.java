package ru.siberteam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.arg.Args;
import ru.siberteam.maker.DictionaryMaker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    private static final Logger LOG = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Args programArgs = new Args();
        if (!programArgs.parse(args)) {
            System.exit(0);
        }
        DictionaryMaker dictionaryMaker = new DictionaryMaker(programArgs.getThreadPool());
        try {
            Files.write(Paths.get(programArgs.getOutputFile()), dictionaryMaker.makeDictionary(programArgs.getURLs()));
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to write to file {}", programArgs.getOutputFile(), e);
        }
    }
}