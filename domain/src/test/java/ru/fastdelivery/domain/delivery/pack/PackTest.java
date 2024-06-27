package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    @Test
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150_001));
        assertThatThrownBy(() -> new Pack(weight))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)));
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }

    @Test
    void whenDimensionsMoreThanMaxDimensions_thenThrowException() {
        BigInteger length = BigInteger.valueOf(1501);
        var dimensions = new Dimensions(length,length,length);
        assertThatThrownBy(() -> new Pack(dimensions))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void whenDimensionsLessThanMaxWeight_thenObjectCreated() {
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)));
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }
}