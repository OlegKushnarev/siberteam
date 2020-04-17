import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (!checkParameters(Arrays.asList(args))) {
            System.exit(0);
        }

        try {
            List<String> strings = getStrings(new FileInputStream(args[0]));
//            List<String> strings = Files.lines(Paths.get(args[0])).collect(Collectors.toList());
            if (strings.isEmpty()) {
                System.out.println("Файл пустой!");
                System.exit(0);
            }

            Map<Character, Integer> map = getCharacterMap(strings);
            if (map.isEmpty()) {
                System.exit(0);
            }

            writeCharacterMap(map, System.out/*new FileOutputStream(args[1])*/);
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

    public static List<String> getStrings(InputStream inputStream) {
        List<String> strings = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (reader.ready()) {
                strings.add(reader.readLine());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return strings;
    }

    public static Map<Character, Integer> getCharacterMap(List<String> strings) {
        Map<Character, Integer> map = new HashMap<>();

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
            int charCount = getCharactersNumber(map.values());

            DecimalFormat outputFormat = new DecimalFormat("##.##");

            for (Map.Entry<Character, Integer> entry : map.entrySet()) {
                double percent = (double) entry.getValue() / charCount * 100;

                String barChar = makeBarChart((int) Math.round(percent), "#");

                bufferedWriter.write(entry.getKey() + " (" + outputFormat.format(percent) + "%): " + barChar + System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getCharactersNumber(Collection<Integer> counts) {
        int count = 0;
        for (Integer number :
                counts) {
            count += number;
        }
        return count;
    }

    public static String makeBarChart(int count, String symbol) {
        StringBuilder barChar = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            barChar.append(symbol);
        }
        return barChar.toString();
    }
}