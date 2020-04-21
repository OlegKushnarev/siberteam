package ru.siberteam.calculators;

import org.apache.log4j.Logger;
import ru.siberteam.barchar.BarChart;
import ru.siberteam.barchar.TextBarChart;
import ru.siberteam.exception.NoResultException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BarChartCalculator implements Calculator {
    private static final Logger log = Logger.getLogger(BarChartCalculator.class);
    //    private final String percentPattern = "\\d{1,2}(\\.\\d*)?";
    private final String percentPattern = "[0-9]*[.,]?[0-9]+";
    private final Calculator calculator;
    private final BarChart barChartCreator;

    public BarChartCalculator(Calculator calculator, BarChart barChartCreator) {
        this.calculator = calculator;
        this.barChartCreator = barChartCreator;
    }

    public BarChartCalculator(Calculator calculator) {
        this.calculator = calculator;
        this.barChartCreator = new TextBarChart("#");
    }

    @Override
    public boolean hasResult() {
        return this.calculator.hasResult();
    }

    @Override
    public Map<Character, String> getResult() throws NoResultException {
        Pattern pattern = Pattern.compile(this.percentPattern);
        return this.calculator.getResult()
                .entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Matcher matcher = pattern.matcher(entry.getValue());
                            if (matcher.find()) {
                                int count = (int) Math.round(Double.parseDouble(matcher.group()));
                                return entry.getValue() + " " + this.barChartCreator.makeBarChart(count);
                            } else {
                                log.debug(this.getClass().getSimpleName() + " " +
                                        entry.getKey() + entry.getValue() +
                                        " Can't create bar chart");
                                return entry.getValue();
                            }
                        }));
    }
}