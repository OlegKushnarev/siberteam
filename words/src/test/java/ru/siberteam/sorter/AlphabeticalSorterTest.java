package ru.siberteam.sorter;

import org.junit.Test;
import ru.siberteam.launcher.SortLauncher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class AlphabeticalSorterTest {

    @Test
    public void testSort() {
        List<String> expected = Arrays.asList("apple", "egg", "father", "HelLo", "mamY", "mother", "rocket",
                "specified", "sun", "tRuck", "енот", "Мам");
        Stream<String> strStream = Stream.of("sun HelLo", "mother apple specified", "father енот", "mother tRuck", "",
                "mamY !@2:$$* egg", "rocket Мам", "fo%nt Mother");
        SortLauncher sortLauncher = new SortLauncher(new AlphabeticalSorter());
        List<String> result = sortLauncher.sortWords(strStream)
                .collect(Collectors.toList());

        assertEquals(expected, result);
    }
}