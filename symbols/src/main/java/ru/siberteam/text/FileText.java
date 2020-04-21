package ru.siberteam.text;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class FileText implements Text {
    private static final Logger log = Logger.getLogger(FileText.class);
    private Iterator<String> textIterator;

    public FileText(String fileName) {
        try {
            List<String> strings = Files.lines(Paths.get(fileName))
                    .collect(Collectors.toList());
            this.textIterator = strings.iterator();
        } catch (IOException e) {
            log.debug("File not found " + e.getMessage());
        }
    }

    @Override
    public boolean hasNextString() {
        return this.textIterator != null && this.textIterator.hasNext();
    }

    @Override
    public String getNextString() {
        return this.textIterator != null ? this.textIterator.next() : "";
    }
}
