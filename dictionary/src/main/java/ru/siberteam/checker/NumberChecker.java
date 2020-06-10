package ru.siberteam.checker;

import org.apache.commons.lang3.StringUtils;
import ru.siberteam.exception.InvalidInputArgException;

import java.util.List;
import java.util.function.Predicate;

public class NumberChecker extends ArgChecker {
    public NumberChecker(String opt) {
        super(opt);
    }

    @Override
    protected void checkOptionValue(List<String> optionValues) throws InvalidInputArgException {
        Predicate<List<String>> isPositiveInteger = values -> values.stream()
                .allMatch(str -> StringUtils.isNumeric(str) && !str.equals("0"));
        if (isPositiveInteger.negate().test(optionValues)) {
            throw new InvalidInputArgException("The number of threads must be a positive integer");
        }
    }
}