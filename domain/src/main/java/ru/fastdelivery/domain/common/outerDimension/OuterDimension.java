package ru.fastdelivery.domain.common.outerDimension;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public record OuterDimension(BigDecimal OuterDimension) {

    private static final BigInteger ROUNDING = new BigInteger("50");
    private static final BigInteger HALF = new BigInteger("2");
    private static final BigInteger LARGE_SIZE = new BigInteger("1500");

    public static boolean isLessThanZero(BigInteger parameter) {
        return BigInteger.ZERO.compareTo(parameter) > 0;
    }

    public static boolean isLargeSize(BigInteger parameter) {
        return parameter.compareTo(LARGE_SIZE) > 0;
    }

    public static BigInteger rounding(BigInteger parameter) {
        parameter = (parameter.remainder(ROUNDING).compareTo(ROUNDING.divide(HALF))) > 0
                ? (((parameter.divide(ROUNDING)).multiply(ROUNDING)).add(ROUNDING))
                : (parameter.divide(ROUNDING)).multiply(ROUNDING);
        return parameter;
    }

    public static OuterDimension zero() {
        return new OuterDimension(BigDecimal.ZERO);
    }

    public static OuterDimension add(BigInteger length, BigInteger width, BigInteger height) {
        if (isLessThanZero(length) || isLessThanZero(width) || isLessThanZero(height)) {
            throw new IllegalArgumentException("Length or width or height cannot be below Zero!");
        }
        if (isLargeSize(length) || isLargeSize(width) || isLargeSize(height)) {
            throw new IllegalArgumentException("The length, width or height cannot be more than 1500 mm.!");
        }
        BigDecimal OuterDimension = cubicMeters(rounding(length), rounding(width), rounding(height));
        return new OuterDimension(OuterDimension);
    }

    public static BigDecimal cubicMeters(BigInteger length, BigInteger width, BigInteger height) {
        BigDecimal cubicMeters = new BigDecimal(length.multiply(width)
                .multiply(height));
        return cubicMeters.divide(BigDecimal.valueOf(1000000000), 4, RoundingMode.HALF_UP);
    }

    public OuterDimension addOuterDimension(OuterDimension OuterDimension) {
        return new OuterDimension(this.OuterDimension().add(OuterDimension.OuterDimension));
    }
}
