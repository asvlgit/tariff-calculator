package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.coordinate.Departure;
import ru.fastdelivery.domain.common.coordinate.Destination;
import ru.fastdelivery.domain.common.coordinate.Distance;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var packages = List.of(new Pack(weight1), new Pack(weight2));
        var shipment = new Shipment(packages,
                new CurrencyFactory(code -> true).create("RUB"), null);

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    void whenSummarizingVolumeOfAllPackages_thenReturnSum() {
        var dimensions1 = new Dimensions(BigInteger.valueOf(1000), BigInteger.valueOf(1000), BigInteger.valueOf(1000));
        var dimensions2 = new Dimensions(BigInteger.ONE, BigInteger.ONE, BigInteger.ONE);

        var packages = List.of(new Pack(dimensions1), new Pack(dimensions2));
        var shipment = new Shipment(packages,
                new CurrencyFactory(code -> true).create("RUB"), null);

        var volumeOfShipment = shipment.volumeAllPackages();

        assertThat(volumeOfShipment).isEqualByComparingTo(BigDecimal.valueOf(1.0001));
    }
}