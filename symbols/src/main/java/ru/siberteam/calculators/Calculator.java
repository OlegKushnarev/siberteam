package ru.siberteam.calculators;

import ru.siberteam.exception.NoResultException;

import java.util.Map;

public interface Calculator {
    boolean hasResult();

    Map<Character, String> getResult() throws NoResultException;
}