import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if (!checkParameters(Arrays.asList(args))) {
            System.exit(0);
        }

        try {
            List<String> strings = Files.lines(Paths.get(args[0]))
                    .collect(Collectors.toList());
            if (strings.isEmpty()) {
                System.out.println("Файл пустой!");
                System.exit(0);
            }

            Map<Character, Integer> map = getCharacterMap(strings);
            if (map.isEmpty()) {
                System.out.println("Неизвестная ошибка!");
                System.exit(0);
            }

            writeCharacterMap(map, new FileOutputStream(args[1]));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkParameters(List<String> list) {
        if (list.isEmpty()) {
            System.out.println("Ошибка! Не указаны имена входного и выходного файлов.");
            return false;
        }

        if (list.size() < 2) {
            System.out.println("Ошибка! Не указано имя выходного файла");
            return false;
        }

        if (list.size() > 2) {
            System.out.println("Ошибка! Указаны неизвестные параметры");
            return false;
        }
        return true;
    }

    public static Map<Character, Integer> getCharacterMap(List<String> strings) {
        Map<Character, Integer> map = new TreeMap<>();

        for (String str :
                strings) {
            for (Character ch :
                    str.toCharArray()) {
                int count = 0;
                if (map.containsKey(ch)) {
                    count = map.get(ch);
                }
                map.put(ch, ++count);
            }
        }
        return map;
    }

    public static void writeCharacterMap(Map<Character, Integer> map, OutputStream outStream) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outStream))) {
            int charCount = map.values().stream()
                    .mapToInt(Integer::intValue)
                    .sum();

            DecimalFormat outputFormat = new DecimalFormat("##.##");

            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                double percent = (double) entry.getValue() / charCount * 100;

                bufferedWriter.write(entry.getKey() + " (" +
                        outputFormat.format(percent) + "%): " +
                        makeBarChart((int) Math.round(percent), "#") +
                        System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String makeBarChart(int count, String symbol) {
        StringBuilder barChar = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            barChar.append(symbol);
        }
        return barChar.toString();
    }
}