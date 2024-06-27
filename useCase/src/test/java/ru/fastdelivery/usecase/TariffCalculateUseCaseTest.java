package ru.fastdelivery.usecase;

import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final PriceProvider priceProvider = mock(PriceProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");

    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(priceProvider);

    @Test
    @DisplayName("Расчет стоимости доставки по весу -> успешно")
    void whenCalculatePriceByWeight_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCbm = new Price(BigDecimal.valueOf(100), currency);

        when(priceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(priceProvider.costPerKg()).thenReturn(pricePerKg);
        when(priceProvider.costPerCbm()).thenReturn(pricePerCbm);

        var shipment = new Shipment(List.of(new Pack(new Weight(BigInteger.valueOf(1200)))),
                new CurrencyFactory(code -> true).create("RUB"));

        var expectedPrice = new Price(BigDecimal.valueOf(120), currency);
        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Расчет стоимости доставки по габаритам -> успешно")
    void whenCalculatePriceByDimensions_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        var pricePerKg = new Price(BigDecimal.valueOf(100), currency);
        var pricePerCbm = new Price(BigDecimal.valueOf(100), currency);

        when(priceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(priceProvider.costPerKg()).thenReturn(pricePerKg);
        when(priceProvider.costPerCbm()).thenReturn(pricePerCbm);

        var shipment = new Shipment(
                List.of(new Pack(
                        new Dimensions(BigInteger.valueOf(1000), BigInteger.valueOf(1000), BigInteger.valueOf(1000)))),
                new CurrencyFactory(code -> true).create("RUB"));

        var expectedPrice = new Price(BigDecimal.valueOf(100), currency);
        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).usingRecursiveComparison()
                .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                .isEqualTo(expectedPrice);
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        BigDecimal minimalValue = BigDecimal.TEN;
        var minimalPrice = new Price(minimalValue, currency);
        when(priceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }
}