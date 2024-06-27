package ru.fastdelivery.domain.common.dimensions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.fastdelivery.domain.common.dimensions.Dimensions;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class DimensionsFactoryTest {

    @ParameterizedTest(name = "Габариты = {arguments} -> объект создан")
    @ValueSource(longs = {0, 1, 100, 10_000})
    void whenDimensionsGreaterThanZero_thenObjectCreated(long length) {
        var dimensions = new Dimensions(BigInteger.valueOf(length), BigInteger.valueOf(length), BigInteger.valueOf(length));

        assertNotNull(dimensions);
        assertThat(dimensions.length()).isEqualByComparingTo(BigInteger.valueOf(length));
    }

    @ParameterizedTest(name = "Габариты = {arguments} -> исключение")
    @ValueSource(longs = {-1, -100, -10_000})
    @DisplayName("Значение габаритов ниже 0.00 -> исключение")
    void whenDimensionsLessThanZero_thenThrowException(long length) {
        assertThatThrownBy(() ->
                new Dimensions(BigInteger.valueOf(length), BigInteger.valueOf(length), BigInteger.valueOf(length))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}