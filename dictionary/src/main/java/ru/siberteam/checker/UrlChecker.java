package ru.siberteam.checker;

import ru.siberteam.exception.InvalidInputArgException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class UrlChecker extends ArgChecker {
    public UrlChecker(String opt) {
        super(opt);
    }

    @Override
    protected void checkOptionValue(List<String> optionValues) throws InvalidInputArgException {
        for (String optionValue : optionValues) {
            try {
                new URL(optionValue);
            } catch (MalformedURLException e) {
                throw new InvalidInputArgException("Invalid URL " + optionValue, e);
            }
        }
    }
}