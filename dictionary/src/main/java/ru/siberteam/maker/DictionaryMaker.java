package ru.siberteam.maker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.siberteam.task.RussianUniqueWords;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DictionaryMaker {
    private static final Logger LOG = LogManager.getLogger(DictionaryMaker.class);

    private ExecutorService executorService;

    public DictionaryMaker(int numberThreads) {
        if (numberThreads < 0) {
            executorService = ForkJoinPool.commonPool();
        } else {
            executorService = new ForkJoinPool(numberThreads);
        }
    }

    private Set<String> getWords(Future<Set<String>> future) {
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("Error in threads.", e);
            throw new IllegalStateException(e);
        }
    }

    public Set<String> makeDictionary(Set<URL> urls) {
        Pattern wordPattern = Pattern.compile("(^[а-яё]{3,}$)");
        Pattern delimiterPattern = Pattern.compile("[.,/#!$%^&*;:{}=\\-_`~()«»\" ]");
        List<RussianUniqueWords> tasks = urls.stream()
                .map(url->new RussianUniqueWords(url, wordPattern, delimiterPattern))
                .collect(Collectors.toList());
        try {
            return executorService.invokeAll(tasks).stream()
                    .flatMap(future->this.getWords(future).stream())
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (InterruptedException e) {
            LOG.error("Error in threads.", e);
            throw new IllegalStateException(e);
        }
    }
}