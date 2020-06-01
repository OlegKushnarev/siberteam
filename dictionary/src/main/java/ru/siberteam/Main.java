package ru.siberteam;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.arg.Args;
import ru.siberteam.compiler.Compiler;

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
        Compiler compiler = new Compiler();
        try {
            Files.write(Paths.get(programArgs.getOutputFile()), compiler.compileDictionary(programArgs.getInputFiles()));
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to write to file:", e);
        }
    }
}