package ru.siberteam.sorter;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class AlphabeticalInvertedWordSorterTest {

    @Test
    public void testSort() {
        List<String> expected = Arrays.asList("deificeps", "elppa", "gge", "kcuRt", "nus", "oLleH", "rehtaf", "rehtom",
                "rehtom", "tekcor", "Ymam", "маМ", "тоне");
        Stream<String> strStream = Stream.of("sun HelLo", "mother apple specified", "father енот", "mother tRuck",
                "mamY !@2:$$* egg", "rocket Мам", "fo%nt")
                .flatMap(str -> Arrays.stream(str.split(" ")));
        Sorter sorter = new AlphabeticalInvertedWordSorter();
        List<String> result = sorter.sortWords(strStream)
                .collect(Collectors.toList());

        assertEquals(expected, result);
    }
}