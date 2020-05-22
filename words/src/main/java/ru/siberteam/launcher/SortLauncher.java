package ru.siberteam.launcher;

import ru.siberteam.sorter.Sorter;
import ru.siberteam.word.Word;

import java.util.Arrays;
import java.util.stream.Stream;

public class SortLauncher {
    private final Sorter sorter;

    public SortLauncher(Sorter sorter) {
        this.sorter = sorter;
    }

    private boolean isWord(String str) {
        return str
                .codePoints()
                .mapToObj(i -> (char) i)
                .allMatch(Character::isLetter);
    }

    public Stream<String> sortWords(Stream<String> stringStream) {
        return stringStream
                .flatMap(str -> Arrays.stream(str.split(" ")))
                .distinct()
                .filter(str -> !str.isEmpty())
                .filter(this::isWord)
                .map(Word::new)
                .distinct()
                .map(Word::getStr)
                .filter(sorter::predicate)
                .map(sorter::mapper)
                .sorted(sorter::comparator);
    }
}
