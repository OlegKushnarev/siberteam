package ru.siberteam.statistics;

import org.apache.log4j.Logger;
import ru.siberteam.calculators.BarChartCalculator;
import ru.siberteam.calculators.Calculator;
import ru.siberteam.calculators.CharacterCalculator;
import ru.siberteam.calculators.PercentCalculator;
import ru.siberteam.exception.NoResultException;
import ru.siberteam.text.Text;

import java.util.Iterator;
import java.util.Map;

public class CharacterStatistics implements Text {
    private static final Logger log = Logger.getLogger(CharacterStatistics.class);
    private Iterator<Map.Entry<Character, String>> entries;

    public CharacterStatistics(Text text) {
        try {
            Calculator calculator = new BarChartCalculator(new PercentCalculator(new CharacterCalculator(text)));
            Map<Character, String> map = calculator.getResult();
            this.entries = map.entrySet().iterator();
        } catch (NoResultException e) {
            log.debug(e.getMessage());
        }
    }

    @Override
    public boolean hasNextString() {
        return this.entries != null && this.entries.hasNext();
    }

    @Override
    public String getNextString() {
        Map.Entry<Character, String> entry = this.entries.next();
        return entry.getKey() + " " +
                entry.getValue() + System.lineSeparator();
    }
}