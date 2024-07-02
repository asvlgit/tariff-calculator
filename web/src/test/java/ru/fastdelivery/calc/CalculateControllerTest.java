package ru.fastdelivery.calc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.fastdelivery.ControllerTest;
import ru.fastdelivery.domain.common.coordinate.*;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

class CalculateControllerTest extends ControllerTest {

    final String baseCalculateApi = "/api/v1/calculate/";
    static final CoordinateRestrictionsPropertiesProvider coordinateRestrictionsPropertiesProvider = mock(CoordinateRestrictionsPropertiesProvider.class);

    @MockBean
    TariffCalculateUseCase useCase;
    @MockBean
    CurrencyFactory currencyFactory;
    @Autowired
    DistanceFactory distanceFactory;

    @TestConfiguration
    static class TestConfig {

        @Bean
        public DistanceFactory distanceFactory() {
            return new DistanceFactory(coordinateRestrictionsPropertiesProvider);
        }
    }

    @Test
    @DisplayName("Валидные данные для расчета стоимость -> Ответ 200")
    void whenValidInputData_thenReturn200() {

        Departure departure = new Departure(new BigDecimal(50), new BigDecimal(50));
        Destination destination = new Destination(new BigDecimal(60), new BigDecimal(60));

        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.TEN, BigInteger.TEN, BigInteger.TEN)),
                "RUB", departure, destination);

        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(coordinateRestrictionsPropertiesProvider.isAvailable(departure.getLatitude(),departure.getLongitude())).thenReturn(true);
        when(coordinateRestrictionsPropertiesProvider.isAvailable(destination.getLatitude(),destination.getLongitude())).thenReturn(true);
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));

        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Список упаковок == null -> Ответ 400")
    void whenEmptyListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(null, "RUB", null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Передача одного из параметров габаритов < 0 -> Ответ 400")
    void whenLessDimensionsListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.TEN, BigInteger.TEN.negate(), BigInteger.TEN)),
                "RUB", null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Передача одного из параметров габаритов > 1500 -> Ответ 400")
    void whenMoreDimensionsListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, BigInteger.TEN, BigInteger.valueOf(1600), BigInteger.TEN)),
                "RUB", null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Передача широты > 65 -> Ответ 400")
    void whenMoreLatitudeListPackages_thenReturn400() {

        Departure departure = new Departure(new BigDecimal(50), new BigDecimal(50));
        Destination destination = new Destination(new BigDecimal(70), new BigDecimal(70));

        var rub = new CurrencyFactory(code -> true).create("RUB");

        when(coordinateRestrictionsPropertiesProvider.isAvailable(departure.getLatitude(),departure.getLongitude())).thenReturn(true);
        when(coordinateRestrictionsPropertiesProvider.isAvailable(destination.getLatitude(),destination.getLongitude())).thenReturn(false);

        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));

        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN,
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(1000))),
                "RUB", departure, destination);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
