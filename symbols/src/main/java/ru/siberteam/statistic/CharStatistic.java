package ru.siberteam.statistic;

import java.text.DecimalFormat;
import java.util.stream.Stream;

public class CharStatistic {
    private static DecimalFormat percentOutputFormat = new DecimalFormat("##.#");

    private final char ch;
    private final int number;
    private final double percent;

    public CharStatistic(char ch, int number, double percent) {
        this.ch = ch;
        this.number = number;
        this.percent = percent;
    }

    public static void setPercentOutputFormat(DecimalFormat outputFormat) {
        percentOutputFormat = outputFormat;
    }

    public int getNumber() {
        return number;
    }

    private String makeBarChart(int count, String symbol) {
        return Stream.of(new StringBuilder()).peek((s) -> {
            for (int i = 0; i < count; ++i) {
                s.append(symbol);
            }
        }).findFirst().get().toString();
    }

    @Override
    public String toString() {
        return this.ch +
                " (" + percentOutputFormat.format(this.percent) + "%) " +
                this.makeBarChart((int) Math.round(this.percent), "#");
    }
}