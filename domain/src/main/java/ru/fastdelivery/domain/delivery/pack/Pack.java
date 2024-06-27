package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * Упаковка груза
 *
 * @param weight     вес товаров в упаковке
 * @param dimensions габариты товаров в упаковке
 */
public record Pack(Weight weight, Dimensions dimensions) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));
    private static final Dimensions MAX_DIMENSIONS = new Dimensions(BigInteger.valueOf(1500), BigInteger.valueOf(1500), BigInteger.valueOf(1500));

    public Pack(Weight weight) {
        this(weight, Dimensions.zero());
    }

    public Pack(Dimensions dimensions) {
        this(Weight.zero(), dimensions);
    }

    public Pack {

        if (weight.greaterThan(maxWeight)) {
            throw new IllegalArgumentException("Package can't be more than " + maxWeight);
        }

        if (dimensions.greaterThan(MAX_DIMENSIONS)) {
            throw new IllegalArgumentException("One or more dimensions cannot be greater than " + MAX_DIMENSIONS);
        }
        //BigInteger lengthRound = dimensions.length().mod(BigInteger.valueOf(50));
        BigInteger lengthRound =
                new BigDecimal(dimensions.length())
                        .divide(BigDecimal.valueOf(50), RoundingMode.UP).toBigInteger()
                        .multiply(BigInteger.valueOf(50));
        BigInteger widthRound =
                new BigDecimal(dimensions.width())
                        .divide(BigDecimal.valueOf(50), RoundingMode.UP).toBigInteger()
                        .multiply(BigInteger.valueOf(50));

        BigInteger heightRound =
                new BigDecimal(dimensions.height())
                        .divide(BigDecimal.valueOf(50), RoundingMode.UP).toBigInteger()
                        .multiply(BigInteger.valueOf(50));

        dimensions = new Dimensions(lengthRound, widthRound, heightRound);
    }
}
