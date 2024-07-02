package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.coordinate.Distance;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;

import java.math.BigDecimal;
import java.util.List;

/**
 * @param packages упаковки в грузе
 * @param currency валюта объявленная для груза
 */
public record Shipment(
        List<Pack> packages,
        Currency currency,
        Distance distance
) {
    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }

    public BigDecimal volumeAllPackages() {
        return packages.stream()
                .map(Pack::dimensions)
                .map(Dimensions::cubicMeters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
