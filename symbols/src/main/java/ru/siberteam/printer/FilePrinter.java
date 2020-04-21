package ru.siberteam.printer;

import org.apache.log4j.Logger;
import ru.siberteam.text.Text;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FilePrinter implements Printer {
    private static final Logger log = Logger.getLogger(FilePrinter.class);
    private final Text text;
    private final String outputFileName;

    public FilePrinter(Text text, String outputFileName) {
        this.text = text;
        this.outputFileName = outputFileName;
    }

    @Override
    public void print() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get(outputFileName))) {
            while (this.text.hasNextString()) {
                bufferedWriter.write(this.text.getNextString());
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        }
    }
}