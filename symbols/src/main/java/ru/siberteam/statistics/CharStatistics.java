package ru.siberteam.statistics;

public class CharStatistics {
    private final char ch;
    private final int number;
    private final double percent;

    public CharStatistics(char ch, int number, double percent) {
        this.ch = ch;
        this.number = number;
        this.percent = percent;
    }

    public char getCh() {
        return ch;
    }

    public int getNumber() {
        return number;
    }

    public double getPercent() {
        return percent;
    }
}