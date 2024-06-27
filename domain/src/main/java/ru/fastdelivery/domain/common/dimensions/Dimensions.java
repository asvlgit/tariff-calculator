package ru.fastdelivery.domain.common.dimensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Габариты
 *
 * @param length Длина
 * @param width  Ширина
 * @param height Высота
 */
public record Dimensions(BigInteger length, BigInteger width, BigInteger height) {

    public Dimensions {

        if (isLessThanZero(length)) {
            throw new IllegalArgumentException("Length cannot be below Zero!");
        }

        if (isLessThanZero(width)) {
            throw new IllegalArgumentException("Width cannot be below Zero!");
        }

        if (isLessThanZero(height)) {
            throw new IllegalArgumentException("Height cannot be below Zero!");
        }

    }

    private static boolean isLessThanZero(BigInteger value) {
        return BigInteger.ZERO.compareTo(value) > 0;
    }

    public static Dimensions zero() {
        return new Dimensions(BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO);
    }

    public BigDecimal volume() {
        return new BigDecimal(height)
                .multiply(new BigDecimal(width))
                .multiply(new BigDecimal(length));
    }

    public BigDecimal cubicMeters() {
        return volume()
                .divide(BigDecimal.valueOf(1_000_000_000), 4, RoundingMode.HALF_UP);
    }

    public boolean greaterThan(Dimensions d) {
        return length().compareTo(d.length()) > 0
                || height().compareTo(d.height()) > 0
                || width().compareTo(d.width()) > 0;
    }
}
