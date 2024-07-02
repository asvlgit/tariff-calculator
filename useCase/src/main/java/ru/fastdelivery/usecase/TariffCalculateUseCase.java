package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private final PriceProvider priceProvider;

    public Price calc(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var volumeAllPackagesCmb = shipment.volumeAllPackages();
        var minimalPrice = priceProvider.minimalPrice();

        Price priceByWeight = priceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg);

        Price priceByVolume = priceProvider
                .costPerCbm()
                .multiply(volumeAllPackagesCmb);

        Price basePrice = priceByWeight.max(priceByVolume).max(minimalPrice);

        BigDecimal distanceAsKilometer = shipment.distance().asKilometer();
        if (distanceAsKilometer.compareTo(BigDecimal.valueOf(450)) > 0) {
            BigDecimal newAmount = basePrice.amount()
                    .multiply(distanceAsKilometer)
                    .divide(BigDecimal.valueOf(450), 50, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            basePrice = new Price(newAmount, basePrice.currency());
        }

        return basePrice;
    }

    public Price minimalPrice() {
        return priceProvider.minimalPrice();
    }
}
