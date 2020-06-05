package ru.siberteam.compiler;

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
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DictionaryMaker {
    private static final Logger LOG = LogManager.getLogger(DictionaryMaker.class);

    private Stream<String> russianWords(String word) {
        String regex = "(^[а-яА-ЯёЁ]{3,}$)";
        Pattern pattern = Pattern.compile(regex);
        return Arrays.stream(word.split(" "))
                .map(str -> str.replaceAll("[.,/#!$%^&*;:{}=\\-_`~()«»\"]", ""))
                .filter(pattern.asPredicate());
    }

    private Set<String> getUniqueWords(URL url) {
        try (InputStream stringStream = url.openStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(stringStream))) {
            return reader.lines()
                    .map(String::toLowerCase)
                    .flatMap(this::russianWords)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to read a file:", e);
            throw new IllegalStateException(e);
        }
    }

    public Set<String> makeDictionary(Set<URL> urls) {
        return urls.parallelStream()
                .unordered()
                .flatMap(url -> getUniqueWords(url).stream())
                .collect(Collectors.toCollection(TreeSet::new));
    }
}