package ru.siberteam.barchar;

public class TextBarChart implements BarChart {
    private String symbol;

    public TextBarChart(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String makeBarChart(int count) {
        StringBuilder barChar = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            barChar.append(this.symbol);
        }
        return barChar.toString();
    }
}