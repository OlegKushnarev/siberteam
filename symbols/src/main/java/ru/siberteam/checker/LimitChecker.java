package ru.siberteam.checker;

import org.apache.commons.lang3.StringUtils;

public class LimitChecker extends ArgChecker {

    public LimitChecker(String opt) {
        super(opt);
    }

    @Override
    public void checkOptionValue(String optionValue) throws IllegalArgumentException {
        if (!(StringUtils.isNumeric(optionValue) &&
                !optionValue.equals("0"))) {
            throw new IllegalArgumentException("The output limit must be a positive integer");
        }
    }
}