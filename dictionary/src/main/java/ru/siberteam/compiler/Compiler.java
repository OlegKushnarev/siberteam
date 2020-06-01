package ru.siberteam.compiler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Compiler {
    private static final Logger LOG = LogManager.getLogger(Compiler.class);

    private boolean isRussianWord(String str) {
        return str.codePoints()
                .allMatch(character -> (character >= 'а' && character <= 'я') || character == 'ё');
    }

    private Set<String> getUniqueWords(Path inputPath) {
        try (Stream<String> stringStream = Files.lines(inputPath)) {
            return stringStream
                    .flatMap(str -> Arrays.stream(str.split(" ")))
                    .filter(str -> str.length() >= 3)
                    .map(String::toLowerCase)
                    .filter(this::isRussianWord)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            LOG.error("Unsuccessful attempt to read a file:", e);
            throw new IllegalStateException(e);
        }
    }

    public List<String> compileDictionary(Set<String> inputFiles) {
        List<String> words = inputFiles.parallelStream()
                .unordered()
                .map(Paths::get)
                .flatMap(path -> getUniqueWords(path).stream())
                .distinct()
                .collect(Collectors.toList());
        try {
            String rule = "< а < б < в < г < д < е < ё < ж < з < и < й < к < л < м < н < о < п < р < с < т < у < ф < х < ц < ч < ш < щ < ъ < ы < ь < я < ю < я";
            RuleBasedCollator collator = new RuleBasedCollator(rule);
            words.sort(collator::compare);
            return words;
        } catch (ParseException e) {
            LOG.error("Error reading sorting rule!", e);
            throw new IllegalStateException(e);
        }
    }
}