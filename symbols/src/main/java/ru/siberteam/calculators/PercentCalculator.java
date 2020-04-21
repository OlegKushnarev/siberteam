package ru.siberteam.calculators;

import org.apache.log4j.Logger;
import ru.siberteam.exception.NoResultException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class PercentCalculator implements Calculator {
    private static final Logger log = Logger.getLogger(PercentCalculator.class);
    private final String numberPattern = "\\d+";
    private final Calculator calculator;
    private int charCount = 0;
    private DecimalFormat outputFormat;

    public PercentCalculator(Calculator calculator) throws NoResultException {
        this.calculator = calculator;
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.getDefault());
        otherSymbols.setDecimalSeparator('.');
        this.outputFormat = new DecimalFormat("##.#", otherSymbols);
        this.charCount = calculator.getResult().values().stream()
                .filter(s -> s.matches(numberPattern))
                .mapToInt(Integer::parseInt)
                .sum();
    }

    public PercentCalculator(Calculator calculator, DecimalFormat outputFormat) throws NoResultException {
        this(calculator);
        this.outputFormat = outputFormat;
    }

    @Override
    public boolean hasResult() {
        return this.charCount > 0;
    }

    @Override
    public Map<Character, String> getResult() throws NoResultException {
        return this.calculator.getResult()
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            if (entry.getValue().matches(numberPattern)) {
                                return "(" + this.outputFormat.format(
                                        (double) Integer.parseInt(entry.getValue()) / this.charCount * 100) +
                                        "%)";
                            } else {
                                log.debug(this.getClass().getSimpleName() + " " +
                                        entry.getKey() + entry.getValue() +
                                        " Can't calculate percent");
                                return entry.getValue();
                            }

                        }));
    }
}