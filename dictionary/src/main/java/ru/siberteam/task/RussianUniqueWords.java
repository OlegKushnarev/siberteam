package ru.siberteam.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.maker.DictionaryMaker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RussianUniqueWords implements Callable<Set<String>> {
    private static final Logger LOG = LogManager.getLogger(DictionaryMaker.class);

    private URL url;

    private Pattern pattern;

    private Pattern delimiterPattern;

    public RussianUniqueWords(URL url, Pattern wordPattern, Pattern delimiterPattern) {
        this.url = url;
        this.pattern = wordPattern;
        this.delimiterPattern = delimiterPattern;
    }

    @Override
    public Set<String> call() {
        try (InputStream stringStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stringStream))) {
            return reader.lines()
                    .map(String::toLowerCase)
                    .flatMap(str -> Arrays.stream(delimiterPattern.split(str)))
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to read a file:", e);
            throw new IllegalStateException(e);
        }
    }
}