package ru.siberteam.checker;

import org.apache.commons.lang3.StringUtils;
import ru.siberteam.exception.InvalidInputArgException;

import java.util.function.Predicate;

public class LimitChecker extends ArgChecker {

    public LimitChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws InvalidInputArgException {
        Predicate<String> isPositiveInteger = str -> StringUtils.isNumeric(str) && !str.equals("0");
        if (isPositiveInteger.negate().test(optionValue)) {
            throw new InvalidInputArgException("The output limit must be a positive integer");
        }
    }
}