package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.fastdelivery.domain.common.coordinate.Departure;
import ru.fastdelivery.domain.common.coordinate.Destination;

import java.util.List;

@Schema(description = "Данные для расчета стоимости доставки")
public record CalculatePackagesRequest(
        @Schema(description = "Список упаковок отправления",
                example = "[{\"weight\": 4056.45}]")
        @NotNull
        @NotEmpty
        List<CargoPackage> packages,

        @Schema(description = "Трехбуквенный код валюты", example = "RUB")
        @NotNull
        String currencyCode,

        @Schema(description = "Координаты пункта отправления",
                example = "\"departure\": { \"latitude\" : 55.446008, \"longitude\" : 65.339151 }")
        @NotNull
        Departure departure,

        @Schema(description = "Координаты пункта получения",
                example = "\"destination\": { \"latitude\" : 73.398660, \"longitude\" : 55.027532 }")
        @NotNull
        Destination destination
) {
}
