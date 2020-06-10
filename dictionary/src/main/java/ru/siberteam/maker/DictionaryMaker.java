package ru.siberteam.maker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryMaker {
    private static final Logger LOG = LogManager.getLogger(DictionaryMaker.class);

    private ForkJoinPool threadPool;

    public DictionaryMaker(ForkJoinPool threadPool) {
        this.threadPool = threadPool;
    }

    private Set<String> getUniqueWords(URL url) {
        try (InputStream stringStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stringStream))) {
            String regex = "(^[а-яё]{3,}$)";
            Pattern pattern = Pattern.compile(regex);
            return reader.lines()
                    .map(String::toLowerCase)
                    .flatMap(str -> Arrays.stream(str.split("[.,/#!$%^&*;:{}=\\-_`~()«»\" ]")))
                    .filter(pattern.asPredicate())
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to read a file:", e);
            throw new IllegalStateException(e);
        }
    }

    public Set<String> makeDictionary(Set<URL> urls) {
        try {
            return threadPool.submit(() -> urls.parallelStream()
                    .flatMap(url -> getUniqueWords(url).stream())
                    .collect(Collectors.toCollection(TreeSet::new))).get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error in threads.", e);
            throw new IllegalStateException(e);
        }
    }
}