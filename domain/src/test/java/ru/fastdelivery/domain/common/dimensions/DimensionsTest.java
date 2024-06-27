package ru.fastdelivery.domain.common.dimensions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DimensionsTest {

    @Test
    @DisplayName("Попытка создать отрицательный габарит -> исключение")
    void whenDimensionsBelowZero_thenException() {
        var length = new BigInteger("-1");
        assertThatThrownBy(() -> new Dimensions(length, length, length))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void equalsTypeWidth_same() {
        var length = new BigInteger("1000");
        var dimensions = new Dimensions(length, length, length);
        var dimensionsSame = new Dimensions(length, length, length);

        assertThat(dimensions)
                .isEqualTo(dimensionsSame)
                .hasSameHashCodeAs(dimensionsSame);
    }

    @Test
    void equalsNull_false() {
        var length = new BigInteger("40");
        var dimensions = new Dimensions(length, length, length);
        assertThat(dimensions).isNotEqualTo(null);
    }

    @ParameterizedTest
    @CsvSource({"1000, 1, true",
            "199, 199, false",
            "50, 999, false"})
    void greaterThanTest(BigInteger low, BigInteger high, boolean expected) {
        var dimensionsLow = new Dimensions(low, low, low);
        var dimensionsHigh = new Dimensions(high, high, high);

        assertThat(dimensionsLow.greaterThan(dimensionsHigh)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Запрос количество кг -> получено корректное значение")
    void whenGetCubicMeters_thenReceiveCbm() {
        BigInteger length = new BigInteger("1000");
        var dimensions = new Dimensions(length, length, length);

        var actual = dimensions.cubicMeters();

        assertThat(actual).isEqualByComparingTo(new BigDecimal("1"));
    }
}