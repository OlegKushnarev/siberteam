package ru.siberteam.sorter;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class VowelNumberSorterTest {

    @Test
    public void testSort() {
        List<String> expected = Arrays.asList("sun", "tRuck", "egg", "Мам", "HelLo", "mother", "apple", "father",
                "енот", "mamY", "rocket", "specified");
        Stream<String> strStream = Stream.of("sun HelLo", "mother apple specified", "father енот", "mother tRuck",
                "mamY !@2:$$* egg", "rocket Мам", "fo%nt");
        Sorter sorter = new VowelNumberSorter();
        List<String> result = sorter.sortWords(strStream)
                .collect(Collectors.toList());

        assertEquals(expected, result);
    }
}