package ru.siberteam.sorter;

import org.junit.Test;
import ru.siberteam.launcher.SortLauncher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class VowelNumberSorterTest {

    @Test
    public void testSort() {
        List<String> expected = Arrays.asList("sun", "truck", "egg", "мам", "hello", "mother", "apple", "father",
                "енот", "mamy", "rocket", "specified");
        Stream<String> strStream = Stream.of("sun HelLo", "mother apple specified", "father енот", "mother tRuck", "",
                "mamY !@2:$$* egg", "rocket Мам", "fo%nt Mother");
        SortLauncher sortLauncher = new SortLauncher(new VowelNumberSorter());
        List<String> result = sortLauncher.launchSort(strStream)
                .collect(Collectors.toList());

        assertEquals(expected, result);
    }
}