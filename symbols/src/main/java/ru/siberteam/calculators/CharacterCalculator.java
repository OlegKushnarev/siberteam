package ru.siberteam.calculators;

import ru.siberteam.exception.NoResultException;
import ru.siberteam.text.Text;

import java.util.Map;
import java.util.TreeMap;

public class CharacterCalculator implements Calculator {
    private final Map<Character, String> map;

    public CharacterCalculator(Text text) {
        this.map = new TreeMap<>();

        while (text.hasNextString()) {
            String nextString = text.getNextString();
            for (Character ch :
                    nextString.toCharArray()) {
                int count = 0;
                if (map.containsKey(ch)) {
                    count = Integer.parseInt(map.get(ch));
                }
                map.put(ch, String.valueOf(++count));
            }
        }
    }

    @Override
    public boolean hasResult() {
        return !this.map.isEmpty();
    }

    @Override
    public Map<Character, String> getResult() throws NoResultException {
        if (!this.map.isEmpty()) {
            return this.map;
        } else {
            throw new NoResultException(this.getClass().getSimpleName() + " No calculation result");
        }
    }
}