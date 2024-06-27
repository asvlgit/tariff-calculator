package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;

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

        return priceByWeight.max(priceByVolume).max(minimalPrice);
    }

    public Price minimalPrice() {
        return priceProvider.minimalPrice();
    }
}
