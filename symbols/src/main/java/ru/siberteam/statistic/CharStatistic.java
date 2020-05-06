package ru.siberteam.statistic;

/**
 * @apiNote This class has a natural ordering that is
 * inconsistent with equals.
 */

public class CharStatistic implements Comparable<CharStatistic> {
    private final char ch;
    private final int number;
    private final double percent;

    public CharStatistic(char ch, int number, double percent) {
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

    @Override
    public int compareTo(CharStatistic o) {
        return o.number - this.number;
    }
}